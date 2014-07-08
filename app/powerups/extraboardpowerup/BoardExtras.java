package powerups.extraboardpowerup;

import models.Club;
import models.PowerupModel;
import play.twirl.api.Html;
import powerups.Powerup;

public class BoardExtras extends Powerup {
    public BoardExtras(Club club, PowerupModel model) {
        super(club, model);
    }

    @Override
    public Html render() {
        return null;
    }
}
