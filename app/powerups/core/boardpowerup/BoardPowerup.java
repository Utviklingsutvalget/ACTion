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
import powerups.models.BoardExtras;

import java.util.*;
import powerups.core.boardpowerup.html.*;

import powerups.models.Board;
import utils.MembershipLevel;
import static play.mvc.Results.ok;

public class BoardPowerup extends Powerup {


    public static final String LEADER = "Leder";
    public static final String VICE = "Nestleder";
    public static final String ECON = "Økonomiansvarlig";
    public static final String EVENT = "Eventansvarlig";

    private Board board;
    public List<BoardMember> boardList;
    private final boolean editable;
    private List<Membership> memberList;

    public BoardPowerup(Club club, PowerupModel model) {
        super(club, model);
        boardList = new ArrayList<>();

        board = Board.find.byId(club.id);

        if(board.leader == null) {
            for(int i = 0; i < 4; i++){

                Integer n = i;
                User dummyUser = new User(n.toString(), "dummy", "dummy",
                        User.Gender.FEMALE, "dummy@gmail.com",
                        "http://www.technobuffalo.com/wp-content/uploads/2012/09/steve-wozniak.jpeg");

                boardList.add(new BoardMember(dummyUser, "title" + i));
            }

        }else{
            boardList.add(new BoardMember(board.leader, LEADER));
            boardList.add(new BoardMember(board.vice, VICE));
            boardList.add(new BoardMember(board.economy, ECON));
            boardList.add(new BoardMember(board.event, EVENT));
        }

        if(board.boardExtra != null){
            for (BoardExtras boardExtras : board.boardExtra) {

                boardList.add(new BoardMember(boardExtras.member, boardExtras.title));
            }
        }

        memberList = new ArrayList<>();

        if(Membership.find.all() != null){
            for (Membership m : Membership.find.all()) {
                if (m.club.equals(club) && m.level.getLevel() >= MembershipLevel.MEMBER.getLevel()) {
                    memberList.add(m);
                }
            }
        }

        // TODO REPLACE WITH WORKING CONTEXT LOGIC AFTER BRANCH MERGE
        editable = true;
        //editable = this.getContext().getMemberLevel().getLevel() == MembershipLevel.VICE.getLevel();
    }

    @Override
    public Html renderAdmin(){
        return admin.render(boardList, true, memberList);
    }

    @Override
    public Html render() {
        return powerup.render(boardList);
    }

    @Override
    public void activate() {
        this.board = new Board();
        this.board.clubId = this.getClub().id;
        for(Membership membership : this.getClub().members) {
            if(membership.level == MembershipLevel.LEADER) {
                board.leader = membership.user;
                break;
            }
        }
        Ebean.save(board);
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
            Map<String, String> existing = new HashMap<>();
            Map<String, String> updates = new HashMap<>();
            Map<String, String> changes;
            List<String> mandatoryPositions = board.getMandatoryPositions();
            Iterator<String> stringIterator = updateContent.fieldNames();

            for (BoardMember member : boardList) {
                existing.put(member.getTitle(), member.getMember().id);
            }

            while (stringIterator.hasNext()) {
                String fieldName = stringIterator.next();
                updates.put(fieldName, updateContent.get(fieldName).asText());
                Logger.warn("key: " + fieldName + " & value: " + updates.get(fieldName));
            }

            changes = getMapUpdates(existing, updates);

            for (String key : changes.keySet()) {

                //boardtitle changed id
                if (mandatoryPositions.contains(key)) {

                    updateMandatory(key, changes.get(key));

                } else {

                    //Boardextra changed id
                    updateExtras(key, changes.get(key));
                }

            }
        }

        return ok("something");
    }

    public void updateExtras(String title, String userId) {

        Logger.info("entered updateExtras, title: " + title + ", userId: " + userId);
        User user = User.find.byId(userId);

        // If user connected to a title is removed and no other user is being given.
        // Delete row with title in entity.
        if((userId == null || userId.equals("")) && title != null){

            for(BoardExtras boardExtras : board.boardExtra){

                if(boardExtras.title.equals(title)){

                    Ebean.delete(boardExtras);
                    return;
                }
            }
        }

        if(title != null && !title.equals("")){

            // Update existing title with new member
            for(BoardExtras boardExtras : board.boardExtra){

                if(boardExtras.title.equals(title) && !boardExtras.member.id.equals(userId)) {

                    boardExtras.setTitle(title, User.find.byId(userId));

                    Ebean.update(board);

                    return;

                }
            }
        }


        if(title != null && !title.equals("")){
            // If the titles sent in is not associated with this board, create new entry in board.boardextra
            if(BoardExtras.findTitlesByBoard(board, title).size() == 0){

                Logger.info("Added new title: " + title + ", connected to userId: " + user.id + ", to boardId: " + board.clubId);
                board.boardExtra.add(new BoardExtras(user, title));

                Ebean.update(board);
            }
        }
    }

    // TODO REVERSE LOGIC TO BE EFFICIENT
    public void updateMandatory(String title, String userId) {
        Map<String, String> columnMap = board.getTitleColumns();
        User user = null;
        for (Membership member : memberList) {
            if (member.user.id.equals(userId)) {
                user = member.user;
                break;
            }
        }

        if (user != null) {
            for (String boardTitle : board.getMandatoryPositions()) {
                if (title.equals(boardTitle)) {
                    this.board.setByName(columnMap.get(boardTitle), user);
                    //Logger.warn("Board: " + board + " & title: " + columnMap.get(boardTitle) + ", & user: " + user.id);
                }
            }

            Ebean.update(board);
        }
    }
}
