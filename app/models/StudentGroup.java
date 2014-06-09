package models;

import com.avaje.ebean.Ebean;
import play.db.ebean.Model;
import play.db.ebean.Transactional;

import javax.persistence.Entity;
import javax.persistence.Id;

import static play.data.validation.Constraints.Required;

@Entity
public class StudentGroup extends Model {
    public static Finder<Long, StudentGroup> find = new Finder<>(Long.class, StudentGroup.class);

    @Transactional
    public static void update(StudentGroup studentGroup) {
        Ebean.update(studentGroup);
    }

    @Id
    public Long id;

    @Required
    public String name;

    @Required
    public String description;
}
