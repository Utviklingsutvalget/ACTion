package powerups;

import models.Club;

import javax.xml.transform.Result;
import java.io.Serializable;

public abstract class Powerup implements Serializable {

    Club club;

    public Powerup(Club club) {

        this.club = club;
    }

    public abstract Result render();
}
