package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;
import powerups.*;

import javax.persistence.*;

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
    @JoinColumn(name = "club_id", insertable = false, updatable = false)
    public Club club;

    @ManyToOne
    @JoinColumn(name = "powerup_id", insertable = false, updatable = false)
    public PowerupModel powerup;

    // The weight specifies in which order items will be rendered. Lower numbers render before higher numbers.
    @Constraints.Required
    public int weight;

    public Activation(Club club, PowerupModel model) {
        this(club, model, 0);
    }

    public Activation(Club club, PowerupModel model, int weight) {
        this.club = club;
        this.powerup = model;
        this.weight = weight;
        this.key = new ActivationKey(club.id, model.id);
    }

    /**
     * The embeddable key used to arrange the relations in this model.
     * @see play.db.ebean.Model
     * @see com.avaje.ebean.Ebean
     */
    @Embeddable
    public class ActivationKey {

        public Long powerupId;

        public Long clubId;

        public ActivationKey(Long clubId, Long powerupId) {
            this.clubId = clubId;
            this.powerupId = powerupId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ActivationKey that = (ActivationKey) o;

            return clubId.equals(that.clubId) && powerupId.equals(that.powerupId);

        }

        @Override
        public int hashCode() {
            int result = powerupId.hashCode();
            result = 31 * result + clubId.hashCode();
            return result;
        }
    }

    public powerups.Powerup getPowerup() {
        return powerups.Powerup.getPowerup(this);
    }
}
