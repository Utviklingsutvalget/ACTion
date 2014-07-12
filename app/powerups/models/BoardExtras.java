package powerups.models;

import models.Club;
import models.Membership;
import models.User;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;

@Entity
public class BoardExtras extends Model {

    @EmbeddedId
    public BoardKey key;

    @ManyToOne
    public Board board;

    @Constraints.Required
    @OneToOne
    @PrimaryKeyJoinColumn
    public User member;

    @Constraints.Required
    public String title;

    @Embeddable
    public class BoardKey {

        public Long clubId;

        public Integer extrasId;

        public boolean equals(Object other) {
            return other == this || other instanceof BoardKey && ((BoardKey) other).clubId.equals(this.clubId);
        }

        public int hashCode() {
            return super.hashCode();
        }
    }

}
