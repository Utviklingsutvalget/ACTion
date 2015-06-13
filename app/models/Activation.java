package models;

import play.data.validation.Constraints;
import powerups.Powerup;

import javax.persistence.*;

/**
 * The Activation model represents an activation of a {@link PowerupModel} that is used to instantiate a
 * {@link Powerup}. The activation includes a reference to the activating club and the activated powerup. Note that
 * powerups are lazy loaded by the {@link Powerup} class, and that the {@link PowerupModel}'s class name therefore has
 * strict naming schemes.
 *
 * @see models.PowerupModel
 * @see powerups.Powerup
 * @see models.Club
 */
@Entity
public class Activation {

    @ManyToOne
    @JoinColumn(name = "club_id", insertable = false, updatable = false)
    private Club club;
    @ManyToOne
    @JoinColumn(name = "powerup_id", insertable = false, updatable = false)
    private PowerupModel powerup;
    // The weight specifies in which order items will be rendered. Lower numbers render before higher numbers.
    @Constraints.Required
    private int weight;
    @EmbeddedId
    private ActivationKey key;

    public Activation(Club club, PowerupModel model) {
        this(club, model, 0);
    }

    public Activation(Club club, PowerupModel model, int weight) {
        this.club = club;
        this.powerup = model;
        this.weight = weight;
        this.key = new ActivationKey(club.getId(), model.getId());
    }

    public ActivationKey getKey() {
        return key;
    }

    public void setKey(final ActivationKey key) {
        this.key = key;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(final Club club) {
        this.club = club;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(final int weight) {
        this.weight = weight;
    }

    public powerups.Powerup getPowerup() {
        return powerups.Powerup.getPowerup(this);
    }

    public void setPowerup(final PowerupModel powerup) {
        this.powerup = powerup;
    }

    public PowerupModel getPowerupModel() {
        return powerup;
    }

    /**
     * The embeddable key used to arrange the relations in this model.
     *
     * @see com.avaje.ebean.Ebean
     */
    @Embeddable
    public class ActivationKey {

        private Long powerupId;
        private Long clubId;

        public ActivationKey(Long clubId, Long powerupId) {
            this.clubId = clubId;
            this.powerupId = powerupId;
        }

        public Long getClubId() {
            return clubId;
        }

        public void setClubId(final Long clubId) {
            this.clubId = clubId;
        }

        public Long getPowerupId() {
            return powerupId;
        }

        public void setPowerupId(final Long powerupId) {
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
}
