package utils;

import com.google.inject.Inject;
import models.clubs.Club;
import models.Membership;
import models.User;
import services.UserService;

/**
 * An objective representation of what a user intends to do.
 */
public class Context {

    @Inject
    private static UserService userService;
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
                if (listMembership.getClub().equals(resource)) {
                    membership = listMembership;
                }
            }
        }
        memberLevel = membership != null ? membership.getLevel() : null;

    }

    /**
     * @return The user who originated the request.
     */
    public User getSender() {
        return sender;
    }

    public static Context getContext(Club club) {
        // TODO Yes, really! This needs fixing

        //User user = userService.getCurrentUser(null);
        return new Context(null, club);

    }

    public MembershipLevel getMemberLevel() {
        if(memberLevel == null) {
            return MembershipLevel.SUBSCRIBE;
        }
        return memberLevel;
    }
}
