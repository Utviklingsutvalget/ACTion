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

    @Constraints.Required
    public String email;

    @Constraints.Required
    public String pictureUrl;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "user")
    public List<Membership> memberships = new ArrayList<>();

    public User(String id, String name, Gender gender, String email, String picureUrl) {

        this.id = id;
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.pictureUrl = picureUrl;
    }

    public static User findById(String id) {return find.where().eq("id", id).findUnique();}

    public static boolean exists(String id) {return find.where().eq("id", id).findRowCount() != 0;}

    @Transactional
    public static void save(User user) {

        if(!exists(user.id))
            Ebean.save(user);
    }
}
