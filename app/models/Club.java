package models;

import com.avaje.ebean.Ebean;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import play.db.ebean.Transactional;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Club extends Model {
    public static Finder<Long, Club> find = new Finder<>(Long.class, Club.class);

    @Transactional
    public static void update(Club club) {
        Ebean.update(club);
    }

    @Id
    public Long id;

    @Constraints.Required
    public String name;

    @Constraints.Required
    public String description;

    @OneToMany(cascade = CascadeType.PERSIST)
    public List<Membership> members = new ArrayList<>();

    @OneToMany(cascade = CascadeType.PERSIST)
    public List<Activation> activations = new ArrayList<>();
}
