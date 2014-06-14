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
public class User extends Model {

    public enum Gender {
        FEMALE, MALE;
    }

    public static Finder<Long, User> find = new Finder<>(
            Long.class, User.class
    );

    @Transactional
    public static void save(User user) {
        Ebean.save(user);
    }

    @Id
    @Constraints.Min(10)
    public Long id;

    @Constraints.Required
    public String name;

    @Constraints.Required
    public Gender gender;

    @OneToMany(cascade= CascadeType.ALL)
    public List<Membership> memberships = new ArrayList<>();

    public User(String name, Gender gender) {
        this.name = name;
        this.gender = gender;
    }
}
