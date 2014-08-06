package utils;

import models.Club;
import models.Membership;
import models.User;

/**
 * An objective representation of what a user intends to do.
 */
public class Context {

    private final User sender;
    private final MembershipLevel memberLevel;

    /**
     *
     * @param sender The user whose action we wish to authorize
     * @param resource The resource the user wishes to perform an action upon
     */
    public Context(User sender, Club resource) {
        this.sender = sender;

        Membership membership = null;
        if(sender != null) {
            for (Membership listMembership : sender.getMemberships()) {
                if (listMembership.club.equals(resource)) {
                    membership = listMembership;
                }
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

        try {
            User user = new Authorize.UserSession().getUser();
            return new Context(user, club);

        } catch(Authorize.SessionException e) {
            return new Context(null, club);
        }
    }

    public MembershipLevel getMemberLevel() {
        if(memberLevel == null) {
            return MembershipLevel.SUBSCRIBE;
        }
        return memberLevel;
    }
}
