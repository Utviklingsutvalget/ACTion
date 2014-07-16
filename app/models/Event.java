package models;

import com.avaje.ebean.Ebean;
import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import play.db.ebean.Transactional;

import javax.persistence.*;
import java.util.Date;

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
    //@Formats.DateTime(pattern="dd-MM-yyyy")
    public Date startTime;

    @Constraints.Required
    //@Formats.DateTime(pattern="dd-MM-yyyy")
    public Date endTime;

    @Constraints.Required
    public String location;

    @Transactional
    public static void update(Event event) {
        Ebean.update(event);
    }
}
