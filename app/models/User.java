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

    public static Finder<String, User> find = new Finder<>(String.class, User.class);

    @Id
    @Constraints.Min(10)
    public String id; //Unique sub id from google

    @Constraints.Required
    public String name;

    @Constraints.Required
    public Gender gender;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "user")
    public List<Membership> memberships = new ArrayList<>();

    public User(String id, String name, Gender gender) {

        this.id = id;
        this.name = name;
        this.gender = gender;
    }

    @Transactional
    public static void save(User user) {Ebean.save(user);}
}
