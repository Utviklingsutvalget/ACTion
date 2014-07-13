package powerups.core.boardpowerup;

import models.Club;
import models.PowerupModel;
import play.twirl.api.Html;
import powerups.Powerup;
import powerups.core.boardpowerup.html.powerup;
import powerups.models.BoardExtras;
import java.util.ArrayList;
import java.util.List;
import powerups.models.Board;

public class BoardPowerup extends Powerup {

    private Board board;
    public List<BoardMember> boardList;

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
    }

    @Override
    public Html render() {
        return powerup.render(boardList);
    }
}
