package powerups.core.recruitpowerup;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import models.Club;
import models.Membership;
import models.PowerupModel;
import models.User;
import play.Logger;
import play.mvc.Result;
import play.twirl.api.Html;
import powerups.Powerup;
import powerups.core.recruitpowerup.html.powerup;
import powerups.models.Pending;
import utils.Authorize;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static play.mvc.Results.ok;


public class RecruitPowerup extends Powerup {

    private Authorize.UserSession session;
    private Club club;
    private boolean isMember;
    private boolean pending;
    private User user;

    public RecruitPowerup(Club club, PowerupModel powerupModel){
        super(club, powerupModel);

        this.club = club;
        this.isMember = false;

        try {
            session = new Authorize.UserSession();
            //alreadyPending();
            user = this.getContext().getSender();

        } catch(Authorize.SessionException e) {}

        if(user != null && club != null){

            if(Membership.find.byId(new Membership(this.club, this.user).id) != null){
                isMember = true;
            }else{
                isMember = false;
            }

            if(Pending.find.byId(new Pending(this.club, this.user).key) != null){
                pending = true;
            }else{
                pending = false;
            }
        }

        Logger.info("pending = " + pending);
        Logger.info("Already member = " + isMember);
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
        return powerup.render(isMember, club, user, pending);
    }

    @Override
    public void activate() {

    }

    @Override
    public Result update(JsonNode updateContent) {

        if(updateContent != null){

            Iterator<String> iter = updateContent.fieldNames();
            Map<String, String> map = new HashMap<>();

            while(iter.hasNext()){

                String key = iter.next();
                String val = updateContent.get(key).asText();
                map.put(key, val);
            }

            for(String key : map.keySet()){

                User user1 = User.find.byId(map.get(key));

                if(user1 != null){
                    Pending pendingUser = new Pending(club, user1, key);

                    Logger.info("Successful insert into pending, id: " + pendingUser.user.id + ", club: " + club.name +
                            ", application_message: " + key);

                    Pending checkForEntry = Pending.find.byId(pendingUser.key);

                    if(checkForEntry == null){

                        Ebean.save(pendingUser);
                    }
                }
            }
        }
        return ok("You successfully submitted for membership");
    }

}
