package models;

import com.avaje.ebean.Ebean;
import static play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.db.ebean.Transactional;

import javax.persistence.*;
import java.util.List;

@Entity
public class Location extends Model {
    public static Finder<Long, Location> find = new Finder<>(Long.class, Location.class);

    @Transactional
    public static void update(Location location) {
        Ebean.update(location);
    }

    @Id
    public long id;

    @Required
    public String name;

    @OneToMany
    public List<Club> clubs;

    @Transient
    public int cssId;


}
