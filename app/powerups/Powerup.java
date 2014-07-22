package powerups;

import com.fasterxml.jackson.databind.JsonNode;
import models.Activation;
import models.Club;
import models.PowerupModel;
import play.twirl.api.Html;
import utils.Context;
import play.mvc.Result;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * The superclass of every Powerup. The nature of a powerup is fickle. Once its class name has been set in its
 * {@link models.PowerupModel}, this should never change post production. The class uses lazy loading to read its
 * model's class name, and thus must be properly set and qualified as documented in the PowerupModel class. Make sure
 * to properly read example powerups' documentation, as well as this one before attempting to use write a powerup.
 * As of 1.0-SNAPSHOT, there is no error handling for the powerup activation, so a user will not get any indication
 * whether this powerup exists if it is not loaded properly.
 * Please use {@link powerups.core.descriptionpowerup.DescriptionPowerup} for reference.
 *
 * Each powerup must define its own view, using the render() method.
 * Each powerup must define its own model(s) for use in the powerup.
 * @since 1.0-SNAPSHOT
 * @see models.PowerupModel
 * @see powerups.core.descriptionpowerup.DescriptionPowerup
 */
public abstract class Powerup implements Serializable {

    /**
     * Private access as no Powerup should ever be able to change the name of a Club.
     */
    private final Club club;

    /**
     * Final, as no view should ever have access to modify its own model runtime.
     */
    public final PowerupModel model;

    /**
     * The request context
     */
    private final Context context;

    /**
     * Boilerplate constructor used to set up a Powerup. This exposes some vital information for all Powerups.
     * As all powerups are instantiated right before the view starts getting served, the implementing constructor of the
     * powerup must readily complete its own objects or models prior to rendering as to avoid interrupting the page
     * load. This way, you may also return an error view if something goes wrong, as long as this is precalculated to
     * only read one variable.
     * @param club The club referenced by the {@link models.Activation} used to instantiate the Powerup. It is saved
     *             for reference.
     * @param model The model referenced by the {@link models.Activation} used to instantiate the Powerup. It is saved
     *              so that views may access the data held by the model.
     * @see play.db.ebean.Model
     */
    public Powerup(Club club, PowerupModel model) {
        this.club = club;
        this.model = model;
        context = Context.getContext(club);
    }

    /**
     * Every powerup must implement a method to render its associated view. Make sure that no logic is done in render(),
     * as render() is called only to serve the {@link play.twirl.api.Html} required by the club's view.
     * For now, only one view may be referenced,
     * though this is likely to change as the API matures.
     * @return The HTML to insert into the club's view.
     * @see views.html.club.show
     * @see play.twirl.api.Html
     */
    public abstract Html render();

    public abstract void activate();

    public abstract Result update(JsonNode updateContent);

    /**
     * The method that a powerup should override if it wants to render a unique view for use in admin panels. Defaults
     * to rendering the default render method, as some powerups may have in-place editing, allowing them to use the
     * same method for admin panels.
     * @return The HTML to insert into the admin panel.
     */
    public Html renderAdmin() {
        return this.render();
    }

    /**
     * Lazily loads the {@link powerups.Powerup} associated with the {@link models.Activation}
     * @param activation The activation used to instantiate the Powerup.
     * @return The instantiated Powerup.
     */
    public static Powerup getPowerup(Activation activation) {
        PowerupModel powerupModel = activation.powerup;
        Club club = activation.club;

        try {
            @SuppressWarnings("unchecked")
            Class<? extends Powerup> c = (Class<? extends Powerup>) Class.forName("powerups." + powerupModel.className);
            Constructor<? extends Powerup> constructor = c.getDeclaredConstructor(Club.class, PowerupModel.class);
            return constructor.newInstance(club, powerupModel);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {

            return new Powerup(club, powerupModel) {
                @Override
                public Html render() {
                    return new Html("Error activating powerup.");
                }

                @Override
                public void activate() {

                }

                @Override
                public Result update(JsonNode updateContent) {
                    return null;
                }

            };
        }
    }

    protected Club getClub() {
        return club;
    }

    public Context getContext() {
        return context;
    }

}
