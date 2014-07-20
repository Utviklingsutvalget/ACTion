package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;
import powerups.*;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * The Activation model represents an activation of a {@link PowerupModel} that is used to instantiate a
 * {@link Powerup}. The activation includes a reference to the activating club and the activated powerup. Note that
 * powerups are lazy loaded by the {@link Powerup} class, and that the {@link PowerupModel}'s class name therefore has
 * strict naming schemes.
 * @see models.PowerupModel
 * @see powerups.Powerup
 * @see models.Club
 */
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

    /**
     * The embeddable key used to arrange the relations in this model.
     * @see play.db.ebean.Model
     * @see com.avaje.ebean.Ebean
     */
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
