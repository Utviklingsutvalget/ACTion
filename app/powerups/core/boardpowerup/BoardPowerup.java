package powerups.core.boardpowerup;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.annotation.Sql;
import com.fasterxml.jackson.databind.JsonNode;
import models.Club;
import models.Membership;
import models.PowerupModel;
import models.User;
import org.json.JSONObject;
import play.mvc.Result;
import play.twirl.api.Html;
import powerups.Powerup;
import powerups.core.boardpowerup.html.powerup;
import powerups.models.BoardExtras;

import java.lang.reflect.Field;
import java.util.*;

import powerups.models.Board;
import utils.MembershipLevel;

import static play.mvc.Results.*;

public class BoardPowerup extends Powerup {

    private Board board;
    public List<BoardMember> boardList;
    public static final String LEADER_STRING = "leder";
    private final boolean editable;
    private List<Membership> memberList;

    public BoardPowerup(Club club, PowerupModel model) {
        super(club, model);
        boardList = new ArrayList<>();

        board = Board.find.byId(club.id);

        boardList.add(new BoardMember(board.leader, "Leder"));
        boardList.add(new BoardMember(board.vice, "Nestleder"));
        boardList.add(new BoardMember(board.economy, "Økonomiansvarlig"));

        for(BoardExtras boardExtras : board.boardExtra){

            boardList.add(new BoardMember(boardExtras.member, boardExtras.title));
        }

        memberList = new ArrayList<>();

        for(Membership m : Membership.find.all()){
            if(m.club.equals(club) && m.level.getLevel() >= MembershipLevel.MEMBER.getLevel()){
                memberList.add(m);
            }
        }


        // TODO REPLACE WITH WORKING CONTEXT LOGIC AFTER BRANCH MERGE
        editable = true;
        //editable = this.getContext().getMemberLevel().getLevel() == MembershipLevel.VICE.getLevel();
    }

    @Override
    public Html render() {
        return powerup.render(boardList, editable, memberList, LEADER_STRING);
    }

    private Map<String, String> getMapUpdates(Map<String, String> existing, Map<String, String> updates) {
        Map<String, String> changes = new HashMap<>();

        for(String key : existing.keySet()) {

            // Delete post if we can and it is empty
            if(!updates.containsKey(key)) {
                changes.put(key, null);
            }

            // Update post if the post is changed
            if(updates.containsKey(key) && !updates.get(key).equals(existing.get(key))) {
                changes.put(key, updates.get(key));
            }
        }

        for(String key : updates.keySet()) {
            if(!existing.containsKey(key)) {
                // Add new posts to key
                changes.put(key, updates.get(key));
            }
        }
        return changes;
    }

    @Override
    public Result update(JsonNode updateContent) {
        if(updateContent != null) {
            Map<String, String> existing = new HashMap<>();
            Map<String, String> updates = new HashMap<>();
            Map<String, String> changes;
            List<String> mandatoryPositions = board.getMandatoryPositions();
            Iterator<String> stringIterator = updateContent.fieldNames();
            for (BoardMember member : boardList) {
                existing.put(member.getTitle(), member.getMember().id);
            }

            while(stringIterator.hasNext()) {
                String fieldName = stringIterator.next();
                updates.put(fieldName, updateContent.get(fieldName).textValue());
            }

            changes = getMapUpdates(existing, updates);

            for(String key : changes.keySet()) {
                if(mandatoryPositions.contains(key)) {
                    updateMandatory(key, changes.get(key));

                } else {

                    updateExtras();
                }
            }
        }






        /*
        if(updateContent != null){

            Iterator<String> iter = updateContent.fieldNames();

            while(iter.hasNext()){

                String fieldName = iter.next();
                String field = updateContent.get(fieldName).asText();

                for(BoardMember b : boardList){

                    if(b.getTitle().equals(field)){

                        if(!(b.getMember().id.equals(fieldName))){

                            String title = b.getTitle();
                            User user = User.find.byId(b.getMember().id);



                            //SHIT HAS BEEN CHANGED
                            b.setTitle("ananas");
                            Ebean.update(b);
                        }
                    }
                }
            }
        }
        */

        return null;

    }

    public void updateExtras(){

    }

    // TODO REVERSE LOGIC TO BE EFFICIENT
    public void updateMandatory(String title, String userId){
        Map<String, String> columnMap = board.getTitleColumns();
        User user = null;
        for(Membership member : memberList) {
            if(member.user.id.equals(userId)) {
                user = member.user;
            }
        }
        if(user != null) {
            for (String boardTitle : board.getMandatoryPositions()) {
                if (title.equals(boardTitle)) {
                    board.setByName(columnMap.get(boardTitle), user);
                }
            }
        }
    }
}
