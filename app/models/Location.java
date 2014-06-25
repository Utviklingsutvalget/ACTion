package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Location extends Model {
    public static Finder<Long, Location> find = new Finder<>(Long.class, Location.class);

    @Id
    public long id;

    @Constraints.Required
    public String name;
}
