package powerups.extraboardpowerup;

import com.fasterxml.jackson.databind.JsonNode;
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

    @Override
    public play.mvc.Result update(JsonNode updateContent) {
        return null;
    }
}
