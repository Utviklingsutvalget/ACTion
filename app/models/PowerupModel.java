package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "powerup")
public class PowerupModel extends Model {
    public static Model.Finder<Long, PowerupModel> find = new Model.Finder<>(Long.class, PowerupModel.class);

    @Id
    public Long id;

    @Constraints.Required
    public String className;

    @Constraints.Required
    public String friendlyName;

    @Constraints.Required
    public boolean isMandatory;

    @Constraints.Required
    public boolean hasMenuEntry;

    @OneToMany(cascade = CascadeType.PERSIST)
    public List<Activation> activations = new ArrayList<>();

}
