package powerups.core.boardpowerup;

import models.Club;
import models.PowerupModel;
import play.twirl.api.Html;
import powerups.Powerup;
import powerups.core.boardpowerup.html.powerup;
import powerups.models.Board;
import powerups.models.BoardExtras;

import java.util.ArrayList;
import java.util.List;

public class BoardPowerup extends Powerup {

    private final Board board;
    List<BoardMember> boardList;

    public BoardPowerup(Club club, PowerupModel model) {
        super(club, model);
        boardList = new ArrayList<>();

        board = Board.find.byId(club.id);

        boardList.add(new BoardMember(board.leader.user, "Leder"));
        boardList.add(new BoardMember(board.vice.user, "Nestleder"));
        boardList.add(new BoardMember(board.economy.user, "Økonomiansvarlig"));

        for(BoardExtras boardExtras : board.boardExtra){

            boardList.add(new BoardMember(boardExtras.member.user, boardExtras.title));
        }

    }

    @Override
    public Html render() {
        return powerup.render();
    }
}
