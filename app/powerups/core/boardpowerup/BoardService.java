package powerups.core.boardpowerup;

import com.avaje.ebean.Ebean;
import models.Club;
import models.Membership;
import models.User;
import play.Logger;
import play.db.ebean.Model;
import powerups.models.Board;
import utils.MembershipLevel;

import java.util.Random;

public class BoardService {
    private static Model.Finder<Board.BoardKey, Board> find = new Model.Finder<>(Board.BoardKey.class, Board.class);
    public static Board getBoard(Club club) throws BoardException {
        Board board = find.byId(new Board(club).key);

        if(board == null) {
            Logger.warn("Making board!");
            board = new Board(club);
            User leader = null;

            for(Membership membership : club.members) {
                if(membership.level == MembershipLevel.LEADER) {
                    leader = membership.user;
                    break;
                }
            }
            if(leader == null) {
                throw new BoardException();
            }
            Ebean.save(board);
        } else {
            Logger.warn("Board exists!");
        }

        if(board.economy == null) {
            Logger.warn("Setting econ!");
            board.economy = getDummyUser();
        }
        if(board.vice == null) {
            Logger.warn("Setting vice!");
            board.vice = getDummyUser();
        }
        if(board.event == null) {
            Logger.warn("Setting event!");
            board.event = getDummyUser();
        }

        if(board.economy == null || board.vice == null || board.event == null) {
            Logger.warn("Throwing exception!");
            throw new BoardException();
        }

        Logger.warn("Returning board!");
        Ebean.update(board);
        return board;
    }

    private static User getDummyUser() {
        User dummy = User.findById("test1234");
        if(dummy == null) {
            Logger.warn("Fucked it up!");
        }
        Logger.warn("Returning dummy");
        return dummy;
    }

    public static Board getBoard(Long clubId) throws BoardException {
        return getBoard(Club.find.byId(clubId));
    }

    public static class BoardException extends Exception {
    }
}
