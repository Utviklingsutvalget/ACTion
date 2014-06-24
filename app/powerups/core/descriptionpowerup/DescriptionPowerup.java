package powerups.core.descriptionpowerup;

import models.Club;
import play.mvc.Result;
import powerups.Powerup;
import powerups.core.descriptionpowerup.html.powerup;
import powerups.models.ClubDescription;

import static play.mvc.Results.ok;

public class DescriptionPowerup extends Powerup {

    private final ClubDescription clubDesc;

    public DescriptionPowerup(Club club) {
        super(club);
        clubDesc = ClubDescription.find.byId(club.id);
    }

    @Override
    public Result render() {
        return ok(powerup.render(clubDesc.description));
    }
}
