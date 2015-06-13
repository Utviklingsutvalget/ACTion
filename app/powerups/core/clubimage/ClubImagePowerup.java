package powerups.core.clubimage;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import models.Club;
import models.PowerupModel;
import play.mvc.Result;
import play.mvc.Results;
import play.twirl.api.Html;
import powerups.Powerup;
import powerups.core.clubimage.html.powerup;
import powerups.models.ClubImage;
import utils.MembershipLevel;
import utils.imaging.ImageLinkValidator;

import java.awt.*;

public class ClubImagePowerup extends Powerup {

    public static final String DEFAULT_IMAGE = "/assets/images/no_club_image.jpg";

    private final ClubImage clubImage;
    @Inject
    private ClubImageService clubImageService;

    public ClubImagePowerup(Club club, PowerupModel model) {
        super(club, model);

        clubImage = clubImageService.findById(new ClubImage(this.getClub(), "").getKey());
    }

    @Override
    public Html render() {
        return powerup.render(this.clubImage.getImageUrl(), false);
    }

    @Override
    public Html renderAdmin() {
        return powerup.render(this.clubImage.getImageUrl(), true);
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
    public Result update(JsonNode updateContent) {
        if (!(this.getContext().getMemberLevel().getLevel() >= MembershipLevel.BOARD.getLevel())) {
            return Results.unauthorized();
        }
        String url = updateContent.get("link").asText();
        ImageLinkValidator validator = new ImageLinkValidator(new Dimension(800, 300), new Dimension(1600, 600));
        ImageLinkValidator.StatusMessage statusMessage = validator.validate(url);

        if (statusMessage.isSuccess()) {
            clubImage.setImageUrl(url);
            Ebean.update(clubImage);
            return Results.ok("Utvalgsbilde endret");
        } else {
            return Results.status(NO_UPDATE, statusMessage.getMessage());
        }
    }

}
