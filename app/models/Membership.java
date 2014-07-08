package models;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;

@Entity
public class Membership extends Model {

    @EmbeddedId
    public MembershipKey id;

    @ManyToOne
    public Club club;

    @ManyToOne
    public User user;

    @Constraints.Required
    public MembershipLevel level;

    public enum MembershipLevel {
        SUBSCRIBE,
        MEMBER,
        BOARD,
        VICE,
        LEADER,
        COUNCIL
    }

    @Embeddable
    public class MembershipKey {

        public Long clubId;

        public String userId;

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
