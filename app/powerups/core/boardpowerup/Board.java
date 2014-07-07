package powerups.core.boardpowerup;

import models.Club;
import play.twirl.api.Html;
import powerups.Powerup;

public class Board extends Powerup {

    public Board(Club club) {
        super(club);


    }

    @Override
    public Html render() {
        return null;
    }
}
