package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The PowerupModel is an Ebean ORM representation of the class used to instatiate a {@link powerups.Powerup}
 * The actual powerup is instantiated by the Powerup class through lazy loading, which is in no way safe unless
 * care is taken to ensure that the className field is entered as noted below. It is imperative that each field for the
 * powerup is filled out precisely, or unpredictable behaviour might occur. This is, as above, due to the behaviour of
 * the class loader.
 * @see powerups.Powerup
 * @see com.avaje.ebean.Ebean
 * @see play.db.ebean.Model
 */
@Entity
@Table(name = "powerup")
public class PowerupModel extends Model {
    public static Model.Finder<Long, PowerupModel> find = new Model.Finder<>(Long.class, PowerupModel.class);

    @Id
    public Long id;

    /**
     * This is the qualified name of the class instantiated by the {@link powerups.Powerup} class within the powerups
     * package. As an example, the DescriptionPowerup class resides in the powerups.core.descriptionpowerup package,
     * so its className should be "core.descriptionpowerup.DescriptionPowerup". Please do not label a powerup as core
     * unless it is intended to be mandatory and used by every club.
     * @see powerups.core.descriptionpowerup.DescriptionPowerup
     */
    @Constraints.Required
    public String className;

    /**
     * This field is used to generate the name of the powerup that appears to the user. It is therefore imperative that
     * this name is filled out as you wish it to appear.
     */
    @Constraints.Required
    public String friendlyName;

    /**
     * This field is used to determine if any new clubs should automatically have this powerup activated for them.
     * @see models.Activation
     * @see models.Club
     */
    @Constraints.Required
    public boolean isMandatory;

    /**
     * This field is used to determine whether or not the powerup's view should be linked to at the top of a club's
     * associated view. Please keep in mind that not all powerups should have a menu entry. For example, we do not
     * require a link that takes us to the club's image. This is also used to determine whether or not this powerup
     * should have a headline above it, though this is subject to change in the future.
     * @see views.html.club.show
     */
    @Constraints.Required
    public boolean hasMenuEntry;

    @Constraints.Required
    public int defaultWeight;

    @OneToMany(cascade = CascadeType.PERSIST)
    public List<Activation> activations = new ArrayList<>();

}
