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
import powerups.core.boardpowerup.html.powerup;
import powerups.core.boardpowerup.html.admin;
import powerups.models.BoardMembership;
import powerups.models.BoardPost;
import utils.MembershipLevel;

import java.util.*;

import static play.mvc.Results.ok;
import static play.mvc.Results.unauthorized;

public class BoardPowerup extends Powerup {

    public static final String EVENT = "Eventansvarlig";
    public List<BoardMembership> boardList;
    private List<Membership> memberList;

    public BoardPowerup(Club club, PowerupModel model) {
        super(club, model);

        if(club != null && model != null){
            boardList = this.getClub().boardMembers;
            memberList = club.members;
            Logger.info("club and model is not null");

        } else {
            boardList = new ArrayList<>();
            memberList = new ArrayList<>();
            Logger.info("club and model is null");
        }
    }

    @Override
    public Html renderAdmin() {
        return admin.render(boardList, true, memberList);
    }

    @Override
    public Html render() {
        return powerup.render(boardList);
    }

    @Override
    public void activate() {
        // TODO ADD LEADER FROM CLUB CREATE
    }


    // Check for new titles, titles with a new associated ID, or titles removed. Return a map of updates
    private Map<String, String> getMapUpdates(Map<String, String> existing, Map<String, String> updates) {
        Map<String, String> changes = new HashMap<>();

        for (String key : existing.keySet()) {
            // Delete post if we can and it is empty
            if (!updates.containsKey(key)) {
                changes.put(key, null);
            }

            // Update post if the post is changed
            if (updates.containsKey(key)) {
                if (!updates.get(key).equals(existing.get(key))) {
                    changes.put(key, updates.get(key));
                    //updates.remove(key);
                }
            }
        }

        for (String key : updates.keySet()) {
            if (!existing.containsKey(key)) {
                // Add new posts to key
                changes.put(key, updates.get(key));
            }
        }
        return changes;
    }

    @Override
    public Result update(JsonNode updateContent) {
        if (updateContent != null) {
            if(updateContent.has("update-type")) {
                if(updateContent.get("update-type").asText().equals("update")) {
                    updateMemberships(updateContent);
                } else if(updateContent.get("update-type").asText().equals("create")) {
                    if(createPost(updateContent)) {
                        return ok("Styrepost opprettet");
                    } else {
                        return unauthorized("Kunne ikke opprette styrepost");
                    }
                } else if(updateContent.get("update-type").asText().equals("add")) {
                    if(addMembership(updateContent)) {
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
        List<BoardPost> boardPosts = BoardPost.find.all();
        for(BoardPost post : boardPosts) {
            if(post.title.equals(updateContent.get("title").asText())) {
                User user = User.find.byId(updateContent.get("user").asText());
                if(user == null) {
                    return false;
                }
                BoardMembership membership = new BoardMembership(this.getClub(), post, user);
                Ebean.save(membership);
                return true;
            }
        }
        return false;
    }

    private boolean createPost(JsonNode updateContent) {
        String title = updateContent.get("title").asText();
        if(title == null) {
            return false;
        }
        List<BoardPost> existing = BoardPost.find.all();
        for(BoardPost existingPost : existing) {
            if(existingPost.title.equals(title)) {
                return false;
            }
        }
        boolean isMandatory = updateContent.get("mandatory").isBoolean();
        int defaultWeight = updateContent.get("weight").asInt();

        BoardPost newPost = new BoardPost(title, isMandatory, defaultWeight);
        Ebean.save(newPost);
        return true;
    }

    private void updateMemberships(JsonNode updateContent) {
        for(BoardMembership membership : this.getClub().boardMembers) {

            // CASE DELETE BOARD MEMBER
            if(updateContent.get(membership.boardPost.title) == null) {
                if(!membership.boardPost.isMandatory) {
                    Ebean.delete(membership);
                } else {
                    unauthorized("Obligatorisk styrepost kan ikke være tom");
                }
            }

            // CASE REPLACE BOARD MEMBER
            if(!updateContent.get(membership.boardPost.title).asText().equals(membership.user.id)) {
                User user = User.findById(updateContent.get(membership.boardPost.title).asText());
                if(user != null) {
                    membership.user = user;
                    Ebean.update(membership);
                }
            }
        }
    }
}
