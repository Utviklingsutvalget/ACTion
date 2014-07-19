package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.annotation.Transactional;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import play.Logger;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import utils.MembershipLevel;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Membership extends Model {

    //public static Finder<Long, Membership> find = new Finder<>(Long.class, Membership.class);

    @Transactional
    public static void update(Membership membership) {
        Ebean.save(membership);
    }

    public static Finder<MembershipKey, Membership> find = new Finder<>(MembershipKey.class, Membership.class);

    public Membership(Club club, User user){
        this.club = club;
        this.user = user;
        this.id = new MembershipKey(club, user);
    }

    @EmbeddedId
    public MembershipKey id;

    @ManyToOne
    public Club club;

    @ManyToOne
    public User user;

    @Constraints.Required
    public MembershipLevel level;

    @Embeddable
    public class MembershipKey {

        public Long clubId;

        public String userId;

        public MembershipKey(Club club, User user){
            this.clubId = club.id;
            this.userId = user.id;
        }

        @Override
        public boolean equals(Object o) {
            if(this == o) {
                return true;
            } else {
                return o instanceof MembershipKey &&
                        ((MembershipKey) o).userId.equals(this.userId) &&
                        ((MembershipKey) o).clubId.equals(this.clubId);
            }
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder()
                    .append(getClass().getName())
                    .toHashCode();
        }
    }

    public static boolean userHasMembershipInClubWithLevel(Long club_id, String user_id, MembershipLevel requiredLevel) {

        Membership membership = find
                .fetch("club")
                .fetch("user")
                .where().eq("club.id", club_id)
                .where().eq("user.id", user_id)
                .findUnique();

        if(membership == null)
            return false;

        if(membership.level.compareTo(requiredLevel) >= 0)
            return true;

        return false;
    }

}
