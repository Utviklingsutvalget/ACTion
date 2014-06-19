package powerups.core.boardpowerup;

import models.Club;
import powerups.Powerup;

import javax.xml.transform.Result;

public class Board extends Powerup {

    public Board(Club club) {
        super(club);


    }

    @Override
    public Result render() {
        return null;
    }
}
