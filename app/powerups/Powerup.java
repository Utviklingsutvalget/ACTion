package powerups;

import models.Club;
import play.twirl.api.Html;

import java.io.Serializable;

public abstract class Powerup implements Serializable {

    Club club;

    public Powerup(Club club) {
        this.club = club;
    }

    public abstract Html render();
}
