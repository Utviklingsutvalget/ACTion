package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Powerup extends Model {
    public static Model.Finder<Long, models.Powerup> find = new Model.Finder<>(Long.class, models.Powerup.class);

    @Id
    public Long id;

    @Constraints.Required
    public String className;

    @Constraints.Required
    public String friendlyName;

    @Constraints.Required
    public boolean isMandatory;

    @Basic(optional = true)
    public boolean hasMenuEntry;

    @OneToMany(cascade = CascadeType.PERSIST)
    public List<Activation> activations = new ArrayList<>();

}
