package models;

import models.composite.ActivationKey;
import play.data.validation.Constraints;
import powerups.Powerup;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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

}
