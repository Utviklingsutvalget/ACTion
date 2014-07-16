package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.annotation.Transactional;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import utils.MembershipLevel;

import javax.persistence.*;

@Entity
public class Membership extends Model {
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

}
