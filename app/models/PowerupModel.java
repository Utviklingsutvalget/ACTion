package models;

import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The PowerupModel is an Ebean ORM representation of the class used to instatiate a {@link powerups.Powerup}
 * The actual powerup is instantiated by the Powerup class through lazy loading, which is in no way safe unless
 * care is taken to ensure that the className field is entered as noted below. It is imperative that each field for the
 * powerup is filled out precisely, or unpredictable behaviour might occur. This is, as above, due to the behaviour of
 * the class loader.
 *
 * @see powerups.Powerup
 * @see com.avaje.ebean.Ebean
 * @see play.db.ebean.Model
 */
@Entity
@Table(name = "powerup")
public class PowerupModel {

    @Id
    private Long id;

    /**
     * This is the qualified name of the class instantiated by the {@link powerups.Powerup} class within the powerups
     * package. As an example, the DescriptionPowerup class resides in the powerups.core.descriptionpowerup package,
     * so its className should be "core.descriptionpowerup.DescriptionPowerup". Please do not label a powerup as core
     * unless it is intended to be mandatory and used by every club.
     *
     * @see powerups.core.descriptionpowerup.DescriptionPowerup
     */
    @Constraints.Required
    private String className;

    /**
     * This field is used to generate the name of the powerup that appears to the user. It is therefore imperative that
     * this name is filled out as you wish it to appear.
     */
    @Constraints.Required
    private String friendlyName;

    /**
     * This field is used to determine if any new clubs should automatically have this powerup activated for them.
     *
     * @see models.Activation
     * @see models.Club
     */
    @Constraints.Required
    private boolean isMandatory;

    /**
     * This field is used to determine whether or not the powerup's view should be linked to at the top of a club's
     * associated view. Please keep in mind that not all powerups should have a menu entry. For example, we do not
     * require a link that takes us to the club's image. This is also used to determine whether or not this powerup
     * should have a headline above it, though this is subject to change in the future.
     *
     * @see views.html.club.show
     */
    @Constraints.Required
    private boolean hasMenuEntry;

    @Constraints.Required
    private int defaultWeight;
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Activation> activations = new ArrayList<>();

    public List<Activation> getActivations() {
        return activations;
    }

    public void setActivations(final List<Activation> activations) {
        this.activations = activations;
    }

    public int getDefaultWeight() {
        return defaultWeight;
    }

    public void setDefaultWeight(final int defaultWeight) {
        this.defaultWeight = defaultWeight;
    }

    public boolean isHasMenuEntry() {
        return hasMenuEntry;
    }

    public void setHasMenuEntry(final boolean hasMenuEntry) {
        this.hasMenuEntry = hasMenuEntry;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(final boolean isMandatory) {
        this.isMandatory = isMandatory;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(final String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(final String className) {
        this.className = className;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

}
