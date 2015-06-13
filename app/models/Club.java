package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.annotation.Transactional;
import play.data.validation.Constraints;
import play.twirl.api.Html;
import powerups.Powerup;
import powerups.core.descriptionpowerup.DescriptionPowerup;
import powerups.models.BoardMembership;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Club {

    @Id
    public Long id;
    @Constraints.Required
    public String name;
    @Constraints.Required
    public String shortName;
    @ManyToOne
    public Location location;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "club")
    public List<BoardMembership> boardMembers = new ArrayList<>();
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "club")
    public List<Membership> members = new ArrayList<>();
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "club")
    public List<Activation> activations = new ArrayList<>();
    @OneToMany
    public List<Event> events = new ArrayList<>();
    @Transient
    public List<Powerup> powerups;
    @OneToMany(cascade = CascadeType.REMOVE)
    private List<Feed> feedPosts = new ArrayList<>();
    @Transient
    private Html listDesc;

    public Club(String name, String shortName, Location location) {
        this.name = name;
        this.shortName = shortName;
        this.location = location;
    }

    @Transactional
    public static void update(Club club) {
        Ebean.update(club);
    }

    @Transactional
    public void delete() {
        deactivatePowerups();
        disInheritEvents();
        Ebean.delete(this);
    }

    public void deactivatePowerups() {
        for (Activation activation : this.activations) {
            Powerup powerup = activation.getPowerup();
            powerup.deActivate();
        }
        Ebean.delete(this.activations);
    }

    public void disInheritEvents() {
        for (Event event : this.events) {
            event.club = null;
            Ebean.update(event);
        }

    }

    // TODO FIND MORE LOGICAL WAY TO IMPLEMENT
    public void setDescriptions() {
        for (Activation activation : activations) {
            Powerup powerup = activation.getPowerup();
            if (powerup instanceof DescriptionPowerup) {
                setListDesc(((DescriptionPowerup) powerup).renderList());
            }
        }
    }

    public Html getListDesc() {
        if (listDesc == null) {
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

    public List<Feed> getFeedPosts() {
        return feedPosts;
    }

    public Long getId() {
        return id;
    }
}
