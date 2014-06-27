package powerups.core.boardpowerup;

import models.Club;
import powerups.Powerup;

public class Board extends Powerup {

    public Board(Club club) {
        super(club);


    }

    @Override
    public play.mvc.Result render() {
        return null;
    }
}
