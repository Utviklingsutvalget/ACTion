package powerups.core.recruitpowerup;

import models.Club;
import models.PowerupModel;
import models.User;
import play.twirl.api.Html;
import powerups.Powerup;
import sun.security.krb5.internal.AuthorizationData;

public class RecruitPowerup extends Powerup {

    private User user;
    private Club club;

    public RecruitPowerup(Club club, PowerupModel powerupModel){
        super(club, powerupModel);

        Authorization.
    }

    @Override
    public Html render(){
        return null;
    }

}
