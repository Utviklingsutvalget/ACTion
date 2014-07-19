package powerups.core.recruitpowerup;

import com.fasterxml.jackson.databind.JsonNode;
import models.Club;
import models.Membership;
import models.PowerupModel;
import play.mvc.Result;
import play.twirl.api.Html;
import powerups.Powerup;
import powerups.core.recruitpowerup.html.powerup;
import powerups.models.Pending;
import utils.Authorize;


public class RecruitPowerup extends Powerup {

    private Authorize.UserSession session;
    private Club club;
    private boolean isMember;

    public RecruitPowerup(Club club, PowerupModel powerupModel){
        super(club, powerupModel);

        this.club = club;
        this.isMember = false;

        try {
            session = new Authorize.UserSession();
            alreadyPending();

        } catch(Authorize.SessionException e) {}
    }

    public boolean alreadyPending(){

        try {

            Pending pendingEntry = Pending.find.byId(new Pending(club, session.getUser()).key);
            Membership membershipEntry = Membership.find.byId(new Membership(club, session.getUser()).id);

            if(pendingEntry != null || membershipEntry != null){
                this.isMember = true;
            }

        } catch(Authorize.SessionException e) {

            //User not logged in
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
