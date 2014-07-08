package powerups.core.clubimage;

import models.Club;
import models.PowerupModel;
import play.twirl.api.Html;
import powerups.Powerup;
import powerups.models.ClubImage;

public class ClubImagePowerup extends Powerup {

    private final ClubImage clubImage;

    public ClubImagePowerup(Club club, PowerupModel model) {
        super(club, model);

        clubImage = ClubImage.find.byId(club.id);
    }

    @Override
    public Html render() {
        return null;
    }
}
