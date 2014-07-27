package powerups.core.boardpowerup;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
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
import utils.MembershipLevel;

import java.util.ArrayList;
import java.util.List;

import static play.mvc.Results.ok;
import static play.mvc.Results.unauthorized;

public class BoardPowerup extends Powerup {

    public static final String LEADER = "Leder";
    public static final String VICE = "Nestleder";
    public static final String EVENT = "Eventansvarlig";
    public static final String ECONOMY = "Økonomiansvarlig";
    public static final String SECRETARY = "Sekretær";
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
    }

    @Override
    public Html renderAdmin() {
        return admin.render(boardList, memberList, posts);
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
                User user = User.find.byId(updateContent.get("user").asText());
                if (user == null) {
                    return false;
                }
                BoardMembership boardMembership = new BoardMembership(this.getClub(), post, user);
                Ebean.save(boardMembership);

                // Loop through the user's memberships to find his or her membership in the club
                Membership membership = Membership.find.byId(new Membership(this.getClub(), user).id);

                // If the user is getting added to the post indicated by LEADER or VICE, set his or her
                // membership level accordingly
                if ((post.title.equals(LEADER) || post.title.equals(VICE))) {
                    if (post.title.equals(LEADER)) {
                        membership.level = MembershipLevel.LEADER;
                    } else if (post.title.equals(VICE)) {
                        membership.level = MembershipLevel.VICE;
                    }

                } else {
                    // If the new rank isn't a flag rank, let's add some levels to the safe list
                    List<MembershipLevel> safeList = new ArrayList<>();
                    safeList.add(MembershipLevel.LEADER);
                    safeList.add(MembershipLevel.VICE);
                    safeList.add(MembershipLevel.COUNCIL);

                    // If the user already has a rank in the safe list, skip this step
                    if (!safeList.contains(membership.level)) {
                        // Set level to BOARD
                        membership.level = MembershipLevel.BOARD;
                    }
                }
                Ebean.update(membership);

                // Returns true if we had a membership to set up
                return true;
            }
        }
        // Returns true if we looped through everything and did nothing
        return false;
    }

    private boolean createPost(JsonNode updateContent) {
        String title = updateContent.get("title").asText();
        if (title == null) {
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

        BoardPost newPost = new BoardPost(title, isMandatory, defaultWeight);
        Ebean.save(newPost);
        return true;
    }

    private boolean deleteMembership(BoardMembership boardMembership) {
        if (boardMembership.boardPost.isMandatory) {
            return false;
        } else {
            Logger.warn("Deleting post");
            boolean userHasOtherPosts = false;

            // Check if the user holding the post has other board memberships
            Logger.warn("Begin looping");
            for(BoardMembership boardMembership2 : this.getClub().boardMembers) {

                Logger.warn("Checking if post is different");
                if(!boardMembership2.boardPost.equals(boardMembership.boardPost)) {
                    Logger.warn("Post was different");

                    Logger.warn("Checking if the user for a different post is the same");
                    if (boardMembership2.user.equals(boardMembership.user)) {
                        Logger.warn("User is the same, returning true");
                        userHasOtherPosts = true;
                        break;
                    }
                }
            }
            // If the user has no other posts, assume he or she should now be a normal member.
            if (!userHasOtherPosts) {
                Membership membership = Membership.find.byId(new Membership(this.getClub(), boardMembership.user).id);
                membership.level = MembershipLevel.MEMBER;
                Ebean.update(membership);
            }

            Ebean.delete(boardMembership);
            return true;
        }
    }

    private Result updateMemberships(JsonNode updateContent) {
        for (BoardMembership boardMembership : this.getClub().boardMembers) {

            // CASE DELETE BOARD MEMBER
            if (updateContent.get(String.valueOf(boardMembership.boardPost.id)).asText().equals("")) {
                if(!deleteMembership(boardMembership)) {
                    return unauthorized("Obligatorisk styrepost kan ikke være tom");
                }
            }
            Logger.warn("Checking to see if post needs update");
            // CASE REPLACE BOARD MEMBER
            if (!updateContent.get(String.valueOf(boardMembership.boardPost.id)).asText().equals(boardMembership.user.id)) {
                User user = User.findById(updateContent.get(String.valueOf(boardMembership.boardPost.id)).asText());
                if (user != null) {
                    Logger.warn("Updating");
                    boardMembership.user = user;
                    Ebean.update(boardMembership);
                }
            }
        }
        return ok("Styreposter oppdatert");
    }
}
