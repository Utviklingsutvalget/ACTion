package models;

import com.avaje.ebean.Ebean;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import play.db.ebean.Transactional;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User extends Model {
    public enum Gender {
        FEMALE, MALE
    }

    public static Finder<Long, User> find = new Finder<>(Long.class, User.class);

    @Id
    @Constraints.Min(10)
    public String id; //Unique sub id from google

    @Constraints.Required
    public String name;

    @Constraints.Required
    public Gender gender;

    public User(String id, String name, Gender gender) {

        this.id = id;
        this.name = name;
        this.gender = gender;
    }

    @Transactional
    public static void save(User user) {

        Ebean.save(user);
    }
}
