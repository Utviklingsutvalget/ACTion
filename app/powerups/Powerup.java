package powerups;

import models.Club;
import play.mvc.Result;

import java.io.Serializable;

public abstract class Powerup implements Serializable {

    Club club;

    public Powerup(Club club) {
        this.club = club;
    }

    public abstract Result render();

    public static void install() {

    }
}
