package powerups.core.recruitpowerup;

import com.fasterxml.jackson.databind.JsonNode;
import models.Club;
import models.PowerupModel;
import models.User;
import play.mvc.Result;
import play.twirl.api.Html;
import powerups.Powerup;

public class RecruitPowerup extends Powerup {

    private User user;

    public RecruitPowerup(Club club, PowerupModel powerupModel){
        super(club, powerupModel);


    }

    @Override
    public Html render(){
        return null;
    }

    @Override
    public Result update(JsonNode updateContent) {
        return null;
    }

}
