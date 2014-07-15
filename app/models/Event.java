package models;

import com.avaje.ebean.Ebean;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import play.db.ebean.Transactional;
import play.twirl.api.Html;
import powerups.*;
import powerups.Powerup;
import powerups.core.descriptionpowerup.DescriptionPowerup;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Event extends Model {
    public static Finder<Long, Event> find = new Finder<>(Long.class, Event.class);

    @Id
    public Long id;

    @Constraints.Required
    public String name;

    @Constraints.Required
    public String description;

    @Constraints.Required
    public Date startTime;

    @Constraints.Required
    public Date endTime;

    @Constraints.Required
    public String location;

    @Transactional
    public static void update(Club club) {
        Ebean.update(club);
    }
}
