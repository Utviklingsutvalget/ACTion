package services;

import com.avaje.ebean.Ebean;
import models.Membership;
import models.composite.ClubUserKey;

public class MembershipService {
    public Membership findById(final ClubUserKey id) {
        return Ebean.find(Membership.class)
                .where()
                .eq("user_id", id.getUserId())
                .eq("club_id", id.getClubId())
                .findUnique();
    }

    public void save(final Membership membership) {
        Ebean.save(membership);
    }
}
