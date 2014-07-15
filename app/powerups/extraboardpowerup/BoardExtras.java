package powerups.extraboardpowerup;

import models.Club;
import models.PowerupModel;
import org.json.JSONObject;
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

    @Override
    public Html update(JSONObject updateContent) {
        return null;
    }
}
