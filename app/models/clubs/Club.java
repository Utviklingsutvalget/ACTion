package models.clubs;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.annotation.Transactional;
import models.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Club {

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String shortName;
    @ManyToOne
    private Location location;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "club")
    private List<Membership> members = new ArrayList<>();
    @OneToMany
    private List<Event> events = new ArrayList<>();
    @OneToMany(cascade = CascadeType.REMOVE)
    private List<Feed> feedPosts = new ArrayList<>();
    @ManyToOne
    private UploadedFile uploadedFile;
    @Column(nullable = true, length = 10000)
    private String description;
    @Column(nullable = true, length = 300)
    private String listDescription;
    @OneToOne
    private User owner;
    @OneToMany(cascade = CascadeType.ALL)
    private List<BoardMembership> boardMemberships;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Pending> pendingMemberships;
    public Club(String name, String shortName, Location location) {
        this.name = name;
        this.shortName = shortName;
        this.location = location;
    }

    @Transactional
    public static void update(Club club) {
        Ebean.update(club);
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(final UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getListDescription() {
        return listDescription;
    }

    public void setListDescription(final String listDescription) {
        this.listDescription = listDescription;
    }

    public List<BoardMembership> getBoardMemberships() {
        return boardMemberships;
    }

    public void setBoardMemberships(final List<BoardMembership> boardMemberships) {
        this.boardMemberships = boardMemberships;
    }

    public List<Pending> getPendingMemberships() {
        return pendingMemberships;
    }

    public void setPendingMemberships(final List<Pending> pendingMemberships) {
        this.pendingMemberships = pendingMemberships;
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

    public List<Membership> getMembers() {
        return members;
    }

    public void setMembers(final List<Membership> members) {
        this.members = members;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(final List<Event> events) {
        this.events = events;
    }


    public void disInheritEvents() {
        for (Event event : this.events) {
            event.setClub(null);
            Ebean.update(event);
        }

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

    public User getOwner() {
        return owner;
    }

    public void setOwner(final User owner) {
        this.owner = owner;
    }
}
