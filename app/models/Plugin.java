package models;

import play.db.ebean.Model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Plugin extends Model {

    @Id
    public String id;

    @OneToMany(cascade = CascadeType.PERSIST)
    public List<Activation> activations = new ArrayList<>();

}
