package powerups.core.descriptionpowerup;

import models.Club;
import models.PowerupModel;
import play.twirl.api.Html;
import powerups.Powerup;
import powerups.core.descriptionpowerup.html.listdesc;
import powerups.core.descriptionpowerup.html.powerup;
import powerups.models.ClubDescription;

import static play.mvc.Results.ok;

public class DescriptionPowerup extends Powerup {

    public static final String NAME = "DescriptionPowerup";

    private final ClubDescription clubDesc;

    public DescriptionPowerup(Club club, PowerupModel model) {
        super(club, model);
        clubDesc = ClubDescription.find.byId(club.id);

    }

    @Override
    public Html render() {
        return powerup.render(clubDesc.description);
    }

    public Html renderList() {
        return listdesc.render(clubDesc.listDescription);
    }
}
