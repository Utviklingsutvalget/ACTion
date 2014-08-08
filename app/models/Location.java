package models;

import com.avaje.ebean.Ebean;
import play.db.ebean.Model;
import play.db.ebean.Transactional;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.List;

import static play.data.validation.Constraints.Required;

@Entity
public class Location extends Model {
    public static Finder<Long, Location> find = new Finder<>(Long.class, Location.class);
    @Id
    public long id;
    @Required
    public String name;
    @OneToMany
    public List<Club> clubs;
    @Transient
    public int cssId;

    @Transactional
    public static void update(Location location) {
        Ebean.update(location);
    }


}
