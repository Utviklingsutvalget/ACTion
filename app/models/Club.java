package models;

import com.avaje.ebean.Ebean;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import play.db.ebean.Transactional;

import javax.persistence.*;
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

    @ManyToOne
    public Location location;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "club")
    public List<Membership> members = new ArrayList<>();

    @OneToMany(cascade = CascadeType.PERSIST)
    public List<Activation> activations = new ArrayList<>();
}
