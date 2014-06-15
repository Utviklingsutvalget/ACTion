package models;

import com.avaje.ebean.Ebean;
import play.db.ebean.Model;
import play.db.ebean.Transactional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static play.data.validation.Constraints.Required;

@Entity
public class Club extends Model {
    public static Finder<Long, Club> find = new Finder<>(Long.class, Club.class);

    @Transactional
    public static void update(Club club) {
        Ebean.update(club);
    }

    @Id
    public Long id;

    @Required
    public String name;

    @Required
    public String description;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "club")
    public List<Membership> members = new ArrayList<>();
}
