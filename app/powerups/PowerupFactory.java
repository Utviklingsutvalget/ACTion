package powerups;

import models.Activation;
import models.Club;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class PowerupFactory {

    public static Powerup getPowerup(Activation activation) {
        models.Powerup powerupModel = models.Powerup.find.byId(activation.key.powerupId);
        Club club = Club.find.byId(activation.key.clubId);

        try {
            Class<? extends Powerup> c = (Class<? extends Powerup>) Class.forName(powerupModel.className);
            Constructor<? extends Powerup> constructor = c.getDeclaredConstructor(Club.class);
            return constructor.newInstance(club);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;

    }
}
