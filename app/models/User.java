package models;

import com.avaje.ebean.Ebean;
import org.hibernate.validator.constraints.Email;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import play.db.ebean.Transactional;

import javax.persistence.*;
import java.util.*;

@Entity
public class User extends Model {

    public static Finder<String, User> find = new Finder<>(String.class, User.class);

    public enum Gender {
        MALE, FEMALE;
    }

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "user")
    public List<Membership> memberships = new ArrayList<>();

    @Id
    @Constraints.Required
    public String id;

    @Constraints.Required
    public String firstName;

    @Constraints.Required
    public String lastName;

    @Constraints.Required
    public Gender gender;

    @Constraints.Required
    @Email
    public String email;

    @Constraints.Required
    public String pictureUrl;

    public User(String id, String firstName, String lastName, Gender gender, String email, String picureUrl) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
        this.pictureUrl = picureUrl;
    }

    public static User findById(String id) {return find.where().eq("id", id).findUnique();}
    public static User findByName(String firstName, String lastName) {
        return find.where().eq("firstName", firstName).eq("lastName", lastName).findUnique();
    }

    public static boolean exists(String id) {return find.where().eq("id", id).findRowCount() != 0;}

    @Transactional
    public static void save(User user) {

        if(!exists(user.id))
            Ebean.save(user);
    }

    @Transactional
    public static void update(User user) {
        Ebean.update(user);
    }

    public static List<String> genderAsList(){
        ArrayList<String> list = new ArrayList<>();

        for(Gender gender : Gender.values()) { list.add(gender.name());}
        return list;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        final User user = (User) o;

        return id.equalsIgnoreCase(user.id);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + id.hashCode();
        return result;
    }
}
