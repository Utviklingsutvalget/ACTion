package powerups.core.clubimage;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import models.Club;
import models.PowerupModel;
import play.twirl.api.Html;
import powerups.Powerup;
import powerups.core.clubimage.html.powerup;
import powerups.models.ClubImage;

public class ClubImagePowerup extends Powerup {

    private final ClubImage clubImage;

    public ClubImagePowerup(Club club, PowerupModel model) {
        super(club, model);

        clubImage = ClubImage.find.byId(club.id);
    }

    @Override
    public Html render() {

        //*********** FOR TESTING PURPOSES ****************
        return powerup.render("http://images4.fanpop.com/image/photos/20100000/Game-of-Thrones-game-of-thrones-20131987-1680-1050.jpg");
    }

    @Override
    public void activate() {
        ClubImage clubImage = new ClubImage();
        clubImage.clubId = this.getClub().id;
        Ebean.save(clubImage);
    }

    @Override
    public play.mvc.Result update(JsonNode updateContent) {
        return null;
    }
}
