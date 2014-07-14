package util;

import models.Club;
import models.Membership;
import models.User;
import play.db.ebean.Model;
import utils.Authorization;

/**
 * An objective representation of what a user intends to do.
 */
public class Context {

    private final User sender;
    private final Membership.MembershipLevel memberLevel;

    /**
     *
     * @param sender The user whose action we wish to authorize
     * @param resource The resource the user wishes to perform an action upon
     * @param action The type of action the user wishes to perform
     */
    public Context(User sender, Club resource) {
        this.sender = sender;

        Membership membership = null;
        for(Membership listMembership : sender.memberships) {
            if(listMembership.club.equals(resource)) {
                membership = listMembership;
            }
        }
        memberLevel = membership != null ? membership.level : null;

    }

    /**
     * @return The user who originated the request.
     */
    public User getSender() {
        return sender;
    }

    public static Context getContext(Club club) {
        //Would this make sense?
        User user = Authorization.authorizeUserSession();
        return new Context(user, club);
    }

    public Membership.MembershipLevel getMemberLevel() {
        return memberLevel;
    }
}
