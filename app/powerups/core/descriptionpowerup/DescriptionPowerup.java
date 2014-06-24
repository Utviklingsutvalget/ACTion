package powerups.core.descriptionpowerup;

import models.Club;
import powerups.Powerup;

import javax.xml.transform.Result;

public class DescriptionPowerup extends Powerup {

    public DescriptionPowerup(Club club) {
        super(club);
    }

    @Override
    public Result render() {
        return null;
    }
}
