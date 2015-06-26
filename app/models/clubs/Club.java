package models.clubs;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.annotation.Transactional;
import models.*;
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
    @GeneratedValue
    private Long id;
    @Constraints.Required
    private String name;
    @Constraints.Required
    private String shortName;
    @ManyToOne
    private Location location;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "club")
    private List<BoardMembership> boardMembers = new ArrayList<>();
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "club")
    private List<Membership> members = new ArrayList<>();
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "club")
    private List<Activation> activations = new ArrayList<>();
    @OneToMany
    private List<Event> events = new ArrayList<>();
    @Transient
    private List<Powerup> powerups;
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

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(final String shortName) {
        this.shortName = shortName;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(final Location location) {
        this.location = location;
    }

    public List<BoardMembership> getBoardMembers() {
        return boardMembers;
    }

    public void setBoardMembers(final List<BoardMembership> boardMembers) {
        this.boardMembers = boardMembers;
    }

    public List<Membership> getMembers() {
        return members;
    }

    public void setMembers(final List<Membership> members) {
        this.members = members;
    }

    public List<Activation> getActivations() {
        return activations;
    }

    public void setActivations(final List<Activation> activations) {
        this.activations = activations;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(final List<Event> events) {
        this.events = events;
    }

    public List<Powerup> getPowerups() {
        return powerups;
    }

    public void setPowerups(final List<Powerup> powerups) {
        this.powerups = powerups;
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
            event.setClub(null);
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

    public void setFeedPosts(final List<Feed> feedPosts) {
        this.feedPosts = feedPosts;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }
}
