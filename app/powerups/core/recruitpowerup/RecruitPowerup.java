package powerups.core.recruitpowerup;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import models.Club;
import models.Membership;
import models.PowerupModel;
import models.User;
import play.Logger;
import play.mvc.Result;
import play.twirl.api.Html;
import powerups.Powerup;
import powerups.core.recruitpowerup.html.admin;
import powerups.core.recruitpowerup.html.powerup;
import powerups.models.Pending;
import services.UserService;
import utils.MembershipLevel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static play.mvc.Results.ok;
import static play.mvc.Results.unauthorized;


public class RecruitPowerup extends Powerup {

    private static final String TERMINATEMEMBERSHIP = "TerminateMemberShip";
    private static final String ACCEPT = "accept";
    private static final String REJECT = "reject";
    private boolean isMember;
    private boolean pending;
    private boolean boardMember;
    private User user;
    private boolean adminAccess = false;
    @Inject
    private UserService userService;

    public RecruitPowerup(Club club, PowerupModel powerupModel) {
        super(club, powerupModel);
        this.isMember = false;
        this.boardMember = false;

        user = this.getContext().getSender();
        Logger.warn("Got sender");
        if (user != null) {
            Logger.warn("Sender exists");
            isMember = this.getContext().getMemberLevel() != null && this.getContext().getMemberLevel() != MembershipLevel.SUBSCRIBE;
            Logger.info("Logged in user is already member = " + isMember);
            pending = Pending.find.byId(new Pending(this.getClub(), this.user).key) != null;
            Logger.info("Logged in user is pending = " + pending);
            if(this.getContext().getMemberLevel().getLevel() >= MembershipLevel.BOARD.getLevel()) {
                boardMember = true;
            }
        }

        Logger.info("Logged in user is pending = " + pending);
        Logger.info("Logged in user is already member = " + isMember);
    }

    @Override
    public Html render() {
        return powerup.render(isMember, this.getClub(), user, pending, boardMember);
    }

    @Override
    public Html renderAdmin() {
        Membership membership = Membership.find.byId(new Membership(this.getClub(), this.getContext().getSender()).id);
        if (this.getContext().getSender().isAdmin() || membership.level.getLevel() >= MembershipLevel.BOARD.getLevel()) {
            return admin.render(this.getClub(), Pending.getByClubId(this.getClub().id));
        } else return this.render();
    }

    @Override
    public void activate() {

    }

    @Override
    public void deActivate() {

    }

    @Override
    public Result update(JsonNode updateContent) {

        if (updateContent != null) {

            Iterator<String> iter = updateContent.fieldNames();
            Map<String, String> map = new HashMap<>();

            boolean firstCheck = true;

            while (iter.hasNext()) {

                String key = iter.next();
                String val = updateContent.get(key).asText();
                map.put(key, val);

                if (val.equals(TERMINATEMEMBERSHIP)) {
                    User terminatedUser = userService.findById(key);
                    Membership terminateMember = Membership.find.byId(new Membership(this.getClub(), terminatedUser).id);

                    if (terminatedUser != null && terminateMember != null) {

                        if (terminateMember.level.getLevel() < MembershipLevel.BOARD.getLevel()) {
                            Ebean.delete(terminateMember);
                            return ok("Du avsluttet medlemsskapet hos " + this.getClub().name);
                        } else {
                            return unauthorized("Kan ikke utføre sletting ettersom din bruker er et styremedlem");
                        }
                    }
                }

                if (val.equals(ACCEPT) || val.equals(REJECT) && (firstCheck)) {
                    firstCheck = false;
                    adminAccess = true;
                }
            }

            if (adminAccess) {
                insertMember(map);

            } else {
                insertToPending(map);
            }
        }
        return ok("You successfully submitted for membership");
    }

    public void insertMember(Map<String, String> map) {

        User user;
        Membership membership;

        for (String key : map.keySet()) {

            user = userService.findById(key);

            if (map.get(key).equals(ACCEPT)) {

                //Pending p = new Pending(club, user);
                Pending oldPending = Pending.find.byId(new Pending(this.getClub(), user).key);

                if (oldPending != null) {
                    Ebean.delete(oldPending);
                }

                membership = Membership.find.byId(new Membership(this.getClub(), user).id);

                if (membership != null) {
                    membership.level = MembershipLevel.MEMBER;
                }

                //membership = new Membership(club, user, MembershipLevel.MEMBER);
                Ebean.save(membership);

                Logger.info("Removed member: " + user.getEmail() + " from pending at clubName: " + this.getClub().name);
                Logger.info("Inserted member: " + user.getEmail() + " in clubName: " + this.getClub().name +
                        " with membershiplevel: " + membership.level);


            } else if (map.get(key).equals(REJECT)) {

                //Pending p = new Pending(club, user);
                Pending oldPending = Pending.find.byId(new Pending(this.getClub(), user).key);

                if (oldPending != null) {
                    Ebean.delete(oldPending);
                }

                Logger.info("Removed Pending user: " + user.getEmail() + " from clubName: " + this.getClub().name);
            }
        }
    }

    public void insertToPending(Map<String, String> map) {

        for (String key : map.keySet()) {

            User user1 = userService.findById(map.get(key));

            if (user1 != null) {
                Pending pendingUser = new Pending(this.getClub(), user1, key);
                Membership subMember = new Membership(this.getClub(), user1, MembershipLevel.SUBSCRIBE);

                Logger.info("Successful insert into pending, id: " + pendingUser.user.getId() + ", club: " + this.getClub().name +
                        ", application_message: " + key);
                Logger.info("Successful insert into membership with subscribe");

                Pending checkForEntry = Pending.find.byId(pendingUser.key);

                if (checkForEntry == null) {

                    Ebean.save(subMember);
                    Ebean.save(pendingUser);
                }
            }
        }
    }
}
