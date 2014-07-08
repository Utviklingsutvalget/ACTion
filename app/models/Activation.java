package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;
import powerups.*;

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

    @ManyToOne
    public PowerupModel powerup;

    // The weight specifies in which order items will be rendered. Lower numbers render before higher numbers.
    @Constraints.Required
    public int weight;

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

    public powerups.Powerup getPowerup() {
        return powerups.Powerup.getPowerup(this);
    }
}
