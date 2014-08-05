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

    public static final String DEFAULT_IMAGE = "/assets/images/no_club_image.jpg";

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
        ClubImage clubImage = new ClubImage(this.getClub(), DEFAULT_IMAGE);
        Ebean.save(clubImage);
    }

    @Override
    public void deActivate() {
        Ebean.delete(clubImage);
    }

    @Override
    public play.mvc.Result update(JsonNode updateContent) {
        if (!(this.getContext().getMemberLevel().getLevel() >= MembershipLevel.BOARD.getLevel())) {
            return Results.unauthorized();
        }
        this.clubImage.imageUrl = updateContent.get("link").asText();
        if(this.clubImage.imageUrl.equals("")) {
            this.clubImage.imageUrl = DEFAULT_IMAGE;
        }
        Ebean.update(clubImage);
        return Results.ok("Bilde endret!");
    }

}
