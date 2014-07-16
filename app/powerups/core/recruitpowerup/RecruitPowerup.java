package powerups.core.recruitpowerup;

import com.fasterxml.jackson.databind.JsonNode;
import models.Club;
import models.Membership;
import models.PowerupModel;
import models.User;
import play.mvc.Result;
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
            alreadyPending();
        }
    }

    public boolean alreadyPending(){

        if(user != null){

            Pending pendingEntry = Pending.find.byId(new Pending(club, user).key);
            Membership membershipEntry = Membership.find.byId(new Membership(club, user).id);

            if(pendingEntry != null || membershipEntry != null){
                this.isMember = true;
            }
        }

        return false;
    }

    @Override
    public Html render(){
        return powerup.render(isMember);
    }

    @Override
    public Result update(JsonNode updateContent) {
        return null;
    }

}
