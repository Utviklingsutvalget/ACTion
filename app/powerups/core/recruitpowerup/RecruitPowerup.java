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

    private Authorization.UserSession session;
    private Club club;
    private boolean isMember;

    public RecruitPowerup(Club club, PowerupModel powerupModel){
        super(club, powerupModel);

        this.club = club;
        this.isMember = false;

        try {
            session = new Authorization.UserSession();
            alreadyPending();

        } catch(Authorization.SessionException e) {}
    }

    public boolean alreadyPending(){

        if(session.isLoggedIn()){

            Pending pendingEntry = Pending.find.byId(new Pending(club, session.getUser()).key);
            Membership membershipEntry = Membership.find.byId(new Membership(club, session.getUser()).id);

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
