package powerups.core.boardpowerup;

import com.fasterxml.jackson.databind.JsonNode;
import models.Club;
import models.PowerupModel;
import play.mvc.Result;
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

    @Override
    public Result update(JsonNode updateContent) {
        return null;
    }
}
