package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

import static play.data.validation.Constraints.Required;

@Entity
public class StudentGroup extends Model {
    public static Finder<Long, StudentGroup> find = new Finder<>(Long.class, StudentGroup.class);

    @Id
    public Long id;

    @Required
    public String name;

    @Required
    public String description;
}
