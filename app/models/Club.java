package models;

import com.avaje.ebean.Ebean;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import play.db.ebean.Transactional;
import play.twirl.api.Html;
import powerups.*;
import powerups.Powerup;
import powerups.core.descriptionpowerup.DescriptionPowerup;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Club extends Model {

    public static Finder<Long, Club> find = new Finder<>(Long.class, Club.class);

    @Transactional
    public static void update(Club club) {
        Ebean.update(club);
    }

    @Id
    public Long id;

    @Constraints.Required
    public String name;

    @Constraints.Required
    public String shortName;

    @ManyToOne
    public Location location;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "club")
    public List<Membership> members = new ArrayList<>();

    @OneToMany(cascade = CascadeType.PERSIST)
    public List<Activation> activations = new ArrayList<>();

    @Transient
    public List<Powerup> powerups;

    @Transient
    private Html listDesc;

    // TODO FIND MORE LOGICAL WAY TO IMPLEMENT
    public void setDescriptions() {
        for(Activation activation : activations) {
            Powerup powerup = activation.getPowerup();
            if(powerup instanceof DescriptionPowerup) {
                setListDesc(((DescriptionPowerup) powerup).renderList());
            }
        }
    }

    public Html getListDesc() {
        if(listDesc == null) {
            setDescriptions();
        }
        return listDesc;
    }

    private void setListDesc(Html listDesc) {
        this.listDesc = listDesc;
    }
}
