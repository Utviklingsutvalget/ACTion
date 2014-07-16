package powerups.core.boardpowerup;

import com.fasterxml.jackson.databind.JsonNode;
import models.Club;
import models.PowerupModel;
import play.mvc.Result;
import play.twirl.api.Html;
import powerups.Powerup;
import powerups.core.boardpowerup.html.powerup;
import powerups.models.BoardExtras;
import java.util.ArrayList;
import java.util.List;
import powerups.models.Board;

import static play.mvc.Results.badRequest;
import static play.mvc.Results.ok;

public class BoardPowerup extends Powerup {

    private Board board;
    public List<BoardMember> boardList;

    private final boolean editable;

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

        // TODO REPLACE WITH WORKING CONTEXT LOGIC AFTER BRANCH MERGE
        editable = true;
        //editable = this.getContext().getMemberLevel().getLevel() == MembershipLevel.VICE.getLevel();
    }

    @Override
    public Html render() {
        return powerup.render(boardList);
    }

    @Override
    public Result update(JsonNode updateContent) {

        // TESTING
        if(updateContent != null){
            return ok("hei");
        }else{
            return badRequest("Error");
        }
    }
}
