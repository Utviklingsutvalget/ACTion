package powerups.core.boardpowerup;

import models.Club;
import models.PowerupModel;
import play.twirl.api.Html;
import powerups.Powerup;

public class Board extends Powerup {

    public Board(Club club, PowerupModel model) {
        super(club, model);


    }

    @Override
    public Html render() {
        return null;
    }
}
