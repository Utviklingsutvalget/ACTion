package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Membership extends Model {

    public enum MembershipLevel {
        SUBSCRIBE (0),
        MEMBER (1),
        BOARD (2),
        VICE (3),
        LEADER (3),
        COUNCIL(5);

        private final int level;
        MembershipLevel(int level) {
            this.level = level;
        }

        public int getLevel() {
            return level;
        }
    }

    @Id
    @ManyToOne
    public User user;

    @Id
    @ManyToOne
    public Club club;

    @Constraints.Required
    public MembershipLevel level;

}
