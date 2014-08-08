package powerups.core.boardpowerup;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import helpers.UserService;
import models.Club;
import models.Membership;
import models.PowerupModel;
import models.User;
import play.Logger;
import play.mvc.Result;
import play.twirl.api.Html;
import powerups.Powerup;
import powerups.core.boardpowerup.html.admin;
import powerups.core.boardpowerup.html.powerup;
import powerups.models.BoardMembership;
import powerups.models.BoardPost;
import utils.Authorize;
import utils.MembershipLevel;

import java.util.ArrayList;
import java.util.List;

import static play.mvc.Results.ok;
import static play.mvc.Results.unauthorized;

public class BoardPowerup extends Powerup {

    public static final String LEADER = "Leder";
    public static final String VICE = "Nestleder";
    private List<BoardMembership> boardList;
    private List<Membership> memberList;
    private List<BoardPost> posts;

    public BoardPowerup(Club club, PowerupModel model) {
        super(club, model);

        if (club != null && model != null) {
            boardList = this.getClub().boardMembers;
            memberList = club.members;
        } else {
            boardList = new ArrayList<>();
            memberList = new ArrayList<>();
        }
        posts = BoardPost.find.all();
        for (BoardMembership membership : boardList) {
            membership.user.onPostLoad();
        }
    }

    @Override
    public Html renderAdmin() {
        Membership membership = Membership.find.byId(new Membership(this.getClub(), this.getContext().getSender()).id);
        if (membership.level == MembershipLevel.LEADER || this.getContext().getSender().isAdmin()) {
            return admin.render(boardList, memberList, posts);
        } else
            return new Html("<div class=\"medium-12 colums text-center\">Styremedlemmer har ikke tilgang til å endre styremedlemmer.</div>");
    }

    @Override
    public Html render() {
        return powerup.render(boardList);
    }

    @Override
    public void activate() {
        List<BoardPost> boardPosts = BoardPost.find.all();
        for (BoardPost post : boardPosts) {
            if (post.title.equals(LEADER)) {
                for (Membership membership : this.getClub().members) {
                    if (membership.level == MembershipLevel.LEADER) {
                        BoardMembership leadership = new BoardMembership(this.getClub(), post, membership.user);
                        Ebean.save(leadership);
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void deActivate() {
        this.boardList.forEach(Ebean::delete);
    }


    @Override
    public Result update(JsonNode updateContent) {
        if (updateContent != null) {
            Logger.warn("BoardPowerup receives update");
            if (updateContent.has("update-type")) {
                Logger.warn("Update type is: " + updateContent.get("update-type").asText());
                if (updateContent.get("update-type").asText().equals("update")) {
                    return updateMemberships(updateContent);
                } else if (updateContent.get("update-type").asText().equals("create")) {
                    if (createPost(updateContent)) {
                        return ok("Styrepost opprettet");
                    } else {
                        return unauthorized("Kunne ikke opprette styrepost");
                    }
                } else if (updateContent.get("update-type").asText().equals("add")) {
                    if (addMembership(updateContent)) {
                        return ok("Styremedlem lagt til");
                    } else {
                        return unauthorized("Kunne ikke legge til styremedlem");
                    }
                }
            }
        }
        return ok("something");
    }

    private boolean addMembership(JsonNode updateContent) {
        for (BoardPost post : posts) {
            if (post.id == (updateContent.get("title").asLong())) {
                User user = UserService.findById(updateContent.get("user").asText());
                if (user == null) {
                    return false;
                }
                BoardMembership boardMembership = new BoardMembership(this.getClub(), post, user);
                Ebean.save(boardMembership);

                this.validateMemberLevels();

                // Returns true if we had a membership to set up
                return true;
            }
        }
        // Returns true if we looped through everything and did nothing
        return false;
    }

    private boolean createPost(JsonNode updateContent) {
        String title = updateContent.get("title").asText();
        title = title.trim();
        if (title.equals("")) {
            return false;
        }
        List<BoardPost> existing = BoardPost.find.all();
        for (BoardPost existingPost : existing) {
            if (existingPost.title.equals(title)) {
                return false;
            }
        }
        boolean isMandatory = updateContent.get("mandatory").asBoolean();
        int defaultWeight = 10;
        boolean createdMandatory = false;

        if (isMandatory) {
            try {
                User user = new Authorize.UserSession().getUser();

                if (user.isAdmin()) {

                    BoardPost newMandatoryPost = new BoardPost(title, true, defaultWeight);
                    createdMandatory = true;
                    Ebean.save(newMandatoryPost);

                }

                return createdMandatory;

            } catch (Authorize.SessionException e) {
                e.printStackTrace();
            }
        }

        BoardPost newPost = new BoardPost(title, isMandatory, defaultWeight);
        Ebean.save(newPost);
        return true;
    }

    private boolean deleteMembership(BoardMembership boardMembership) {
        if (boardMembership.boardPost.isMandatory) {
            return false;
        } else {
            Logger.warn("Deleting post");

            Ebean.delete(boardMembership);
            this.validateMemberLevels();
            return true;
        }
    }

    private boolean userHasOtherPosts(BoardMembership boardMembership, User user) {
        boolean userHasOtherPosts = false;

        // Check if the user holding the post has other board memberships
        Logger.warn("Begin looping");
        for (BoardMembership boardMembership2 : this.getClub().boardMembers) {

            Logger.warn("Checking if post is different");
            if (!boardMembership2.boardPost.equals(boardMembership.boardPost)) {
                Logger.warn("Post was different");

                Logger.warn("Checking if the user for a different post is the same");
                if (boardMembership2.user.equals(user)) {
                    Logger.warn("User is the same, returning true");
                    userHasOtherPosts = true;
                    break;
                }
            }
        }
        return userHasOtherPosts;
    }

    private boolean userHasOtherPosts(BoardMembership boardMembership) {
        User user = boardMembership.user;
        return userHasOtherPosts(boardMembership, user);
    }

    private Result updateMemberships(JsonNode updateContent) {
        for (BoardMembership boardMembership : this.getClub().boardMembers) {

            // CASE DELETE BOARD MEMBER
            if (updateContent.get(String.valueOf(boardMembership.boardPost.id)).asText().equals("")) {
                if (!deleteMembership(boardMembership)) {
                    return unauthorized("Obligatorisk styrepost kan ikke være tom");
                }
            }
            Logger.warn("Checking to see if post needs update");
            // CASE REPLACE BOARD MEMBER
            if (!updateContent.get(String.valueOf(boardMembership.boardPost.id)).asText().equals(boardMembership.user.getId())) {
                User user = UserService.findById(updateContent.get(String.valueOf(boardMembership.boardPost.id)).asText());
                if (user != null) {
                    Logger.warn("Updating");
                    boardMembership.user = user;
                    Ebean.update(boardMembership);
                }
            }
        }
        validateMemberLevels();
        return ok("Styreposter oppdatert");
    }

    private void validateMemberLevels() {
        for (Membership membership : this.getClub().members) {
            Logger.warn("Checking memberships for " + membership.user.getFirstName());
            if (membership.level != MembershipLevel.SUBSCRIBE && membership.level != MembershipLevel.COUNCIL) {

                boolean levelChanged = false;
                for (BoardMembership boardMembership : this.getClub().boardMembers) {
                    if (boardMembership.user.equals(membership.user)) {
                        Logger.warn("Found " + membership.user.getFirstName() + " to be a board member");
                        // We now know this user is at least a board member.
                        if (!levelChanged) {
                            levelChanged = true;
                            membership.level = MembershipLevel.BOARD;
                        }

                        String postTitle = boardMembership.boardPost.title;
                        if (postTitle.equals(LEADER)) {
                            levelChanged = true;
                            membership.level = MembershipLevel.LEADER;
                            break;
                        } else if (postTitle.equals(VICE)) {
                            levelChanged = true;
                            membership.level = MembershipLevel.VICE;
                            // We also break for VICE as noone can be both leader and vice.
                            break;
                        }
                    }
                }

                if (levelChanged) {
                    Ebean.update(membership);
                } else {
                    membership.level = MembershipLevel.MEMBER;
                    Ebean.update(membership);
                }
            }
        }
    }
}
