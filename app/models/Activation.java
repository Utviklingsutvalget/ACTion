package models;

import play.db.ebean.Model;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Activation extends Model {

    @EmbeddedId
    public ActivationKey key;

    @ManyToOne
    public Club club;

    @Embeddable
    public class ActivationKey {

        public Long powerupId;

        public Long clubId;

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else {
                return o instanceof ActivationKey &&
                        ((ActivationKey) o).powerupId.equals(this.powerupId) &&
                        ((ActivationKey) o).clubId.equals(this.clubId);
            }
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

    }
}
