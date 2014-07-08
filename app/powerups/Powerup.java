package powerups;

import models.Activation;
import models.Club;
import play.twirl.api.Html;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class Powerup implements Serializable {

    Club club;

    public Powerup(Club club) {
        this.club = club;
    }

    public abstract Html render();

    public static Powerup getPowerup(Activation activation) {
        models.Powerup powerupModel = models.Powerup.find.byId(activation.key.powerupId);
        Club club = Club.find.byId(activation.key.clubId);

        try {
            @SuppressWarnings("unchecked")
            Class<? extends Powerup> c = (Class<? extends Powerup>) Class.forName("powerups." + powerupModel.className);
            Constructor<? extends Powerup> constructor = c.getDeclaredConstructor(Club.class);
            return constructor.newInstance(club);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
