package powerups.core.recruitpowerup;

import com.avaje.ebean.Ebean;
import models.Club;
import models.PowerupModel;
import models.User;
import play.twirl.api.Html;
import powerups.Powerup;
import powerups.core.recruitpowerup.html.powerup;
import powerups.models.Pending;
import utils.Authorization;

public class RecruitPowerup extends Powerup {

    private User user;
    private Club club;

    public RecruitPowerup(Club club, PowerupModel powerupModel){
        super(club, powerupModel);

        this.club = club;
    }

    public void insertToPending(){

        User loggedInUser = Authorization.authorizeUserSession();

        if(loggedInUser != null){

            user = User.find.byId(loggedInUser.id);
            Ebean.update(new Pending(club, user));

        }
    }

    @Override
    public Html render(){
        return powerup.render();
    }

}
