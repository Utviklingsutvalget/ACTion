package powerups.core.clubimage;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import models.Club;
import models.PowerupModel;
import play.mvc.Results;
import play.twirl.api.Html;
import powerups.Powerup;
import powerups.core.clubimage.html.powerup;
import powerups.models.ClubImage;
import utils.MembershipLevel;

public class ClubImagePowerup extends Powerup {

    private final ClubImage clubImage;

    public ClubImagePowerup(Club club, PowerupModel model) {
        super(club, model);

        clubImage = ClubImage.find.byId(new ClubImage(this.getClub(), "").key);
    }

    @Override
    public Html render() {
        return powerup.render(this.clubImage.imageUrl, false);
    }

    @Override
    public Html renderAdmin() {
        return powerup.render(this.clubImage.imageUrl, true);
    }

    @Override
    public void activate() {
        ClubImage clubImage = new ClubImage(this.getClub(), "");
        Ebean.save(clubImage);
    }

    @Override
    public play.mvc.Result update(JsonNode updateContent) {
        if (!(this.getContext().getMemberLevel().getLevel() >= MembershipLevel.BOARD.getLevel())) {
            return Results.unauthorized();
        }
        this.clubImage.imageUrl = updateContent.get("link").asText();
        Ebean.update(clubImage);
        return Results.ok();
    }

}
