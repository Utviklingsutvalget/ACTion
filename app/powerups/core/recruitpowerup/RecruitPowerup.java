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
    private boolean isMember;

    public RecruitPowerup(Club club, PowerupModel powerupModel){
        super(club, powerupModel);

        this.club = club;
        this.isMember = false;

        user = Authorization.authorizeUserSession();

        if(user != null){
            alreadyMember();
        }
    }

    public boolean alreadyMember(){

        if(user != null){

            Pending entry = Pending.find.byId(new Pending(club, user).key);

            if(entry != null){
                this.isMember = true;
            }
        }

        return false;
    }

    public void insertToPending(){

        if(user != null){

            Ebean.update(new Pending(club, user));
        }
    }

    @Override
    public Html render(){
        return powerup.render(isMember);
    }

}
