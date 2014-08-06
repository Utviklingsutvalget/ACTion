package powerups.core.recruitpowerup;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import helpers.UserService;
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
import utils.Authorize;
import utils.MembershipLevel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static play.mvc.Results.ok;
import static play.mvc.Results.unauthorized;


public class RecruitPowerup extends Powerup {

    private static final String TERMINATEMEMBERSHIP = "TerminateMemberShip";
    private static final String ACCEPT = "accept";
    private static final String REJECT = "reject";
    private Authorize.UserSession session;
    private Club club;
    private boolean isMember;
    private boolean pending;
    private User user;
    private boolean adminAccess = false;

    public RecruitPowerup(Club club, PowerupModel powerupModel) {
        super(club, powerupModel);

        this.club = club;
        this.isMember = false;

        try {
            session = new Authorize.UserSession();
            user = this.getContext().getSender();

        } catch (Authorize.SessionException ignored) {
        }

        if (user != null && club != null) {

            isMember = this.getContext().getMemberLevel() != null && this.getContext().getMemberLevel() != MembershipLevel.SUBSCRIBE;

            pending = Pending.find.byId(new Pending(this.club, this.user).key) != null;
        }

        Logger.info("Logged in user is pending = " + pending);
        Logger.info("Logged in user is already member = " + isMember);
    }

    @Override
    public Html render() {
        return powerup.render(isMember, club, user, pending);
    }

    @Override
    public Html renderAdmin() {
        Membership membership = Membership.find.byId(new Membership(this.getClub(), this.getContext().getSender()).id);
        if (membership.level.getLevel() >= MembershipLevel.BOARD.getLevel()) {
            return admin.render(club, Pending.getByClubId(club.id));
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
                    User terminatedUser = UserService.findById(key);
                    Membership terminateMember = Membership.find.byId(new Membership(club, terminatedUser).id);

                    if (terminatedUser != null && terminateMember != null) {

                        if (terminateMember.level.getLevel() < MembershipLevel.BOARD.getLevel()) {
                            Ebean.delete(terminateMember);
                            return ok("Du avsluttet medlemsskapet hos " + club.name);
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

            user = UserService.findById(key);

            if (map.get(key).equals(ACCEPT)) {

                //Pending p = new Pending(club, user);
                Pending oldPending = Pending.find.byId(new Pending(club, user).key);

                if (oldPending != null) {
                    Ebean.delete(oldPending);
                }

                membership = Membership.find.byId(new Membership(club, user).id);

                if (membership != null) {
                    membership.level = MembershipLevel.MEMBER;
                }

                //membership = new Membership(club, user, MembershipLevel.MEMBER);
                Ebean.save(membership);

                Logger.info("Removed member: " + user.getEmail() + " from pending at clubName: " + club.name);
                Logger.info("Inserted member: " + user.getEmail() + " in clubName: " + club.name +
                        " with membershiplevel: " + membership.level);


            } else if (map.get(key).equals(REJECT)) {

                //Pending p = new Pending(club, user);
                Pending oldPending = Pending.find.byId(new Pending(club, user).key);

                if (oldPending != null) {
                    Ebean.delete(oldPending);
                }

                Logger.info("Removed Pending user: " + user.getEmail() + " from clubName: " + club.name);
            }
        }
    }

    public void insertToPending(Map<String, String> map) {

        for (String key : map.keySet()) {

            User user1 = UserService.findById(map.get(key));

            if (user1 != null) {
                Pending pendingUser = new Pending(club, user1, key);
                Membership subMember = new Membership(club, user1, MembershipLevel.SUBSCRIBE);

                Logger.info("Successful insert into pending, id: " + pendingUser.user.getId() + ", club: " + club.name +
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
