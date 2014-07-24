package models;

import com.avaje.ebean.Ebean;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import play.db.ebean.Transactional;
import play.twirl.api.Html;
import powerups.*;
import powerups.Powerup;
import powerups.core.descriptionpowerup.DescriptionPowerup;
import powerups.models.BoardMembership;
import utils.MembershipLevel;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "club")
    public List<BoardMembership> boardMembers = new ArrayList<>();

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "club")
    public List<Membership> members = new ArrayList<>();

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "club")
    public List<Activation> activations = new ArrayList<>();

    @OneToMany
    public List<Event> events = new ArrayList<>();

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

    public Club(String name, String shortName, Location location) {
        this.name = name;
        this.shortName = shortName;
        this.location = location;
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

    public int getNumberOfMembers() {
        return members.size();
    }
}
