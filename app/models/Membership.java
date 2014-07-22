package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.annotation.Transactional;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import play.Logger;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import utils.MembershipLevel;

import javax.persistence.*;
import java.nio.channels.MembershipKey;
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
        this(club, user, MembershipLevel.MEMBER);
    }

    public Membership(Club club, User user, MembershipLevel level) {
        this.club = club;
        this.user = user;
        this.level = level;
        this.id = new MembershipKey(club, user);
    }

    @EmbeddedId
    public MembershipKey id;

    @ManyToOne
    @JoinColumn(name = "club_id", insertable = false, updatable = false)
    public Club club;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
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
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MembershipKey that = (MembershipKey) o;

            return clubId.equals(that.clubId) && userId.equals(that.userId);

        }

        @Override
        public int hashCode() {
            int result = clubId.hashCode();
            result = 31 * result + userId.hashCode();
            return result;
        }
    }

}
