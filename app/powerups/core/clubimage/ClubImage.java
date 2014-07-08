package powerups.core.clubimage;

import models.Club;
import play.twirl.api.Html;
import powerups.Powerup;

public class ClubImage extends Powerup {

    private final ClubImage clubImage;

    public ClubImage(Club club) {
        super(club);

        clubImage = club.;
    }

    @Override
    public Html render() {
        return null;
    }
}
