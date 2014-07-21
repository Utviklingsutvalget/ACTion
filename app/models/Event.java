package models;

import com.google.api.client.util.DateTime;
import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Event extends Model {

    public static Finder<Long, Event> find = new Finder<>(Long.class, Event.class);
    @Id
    public Long id;
    @Constraints.Required
    public Privacy privacy;
    @Constraints.Required
    public String name;
    @Constraints.Required
    public String description;
    @Constraints.Required
    public Date startTime;
    @Constraints.Required
    public Date endTime;
    @Constraints.Required
    public String location;
    public String coverUrl;
    @ManyToOne
    public Club club;
    @OneToMany(mappedBy = "event")
    public List<Participation> participants = new ArrayList<>();
    @Transient
    private String timeString;
    @Transient
    private String dateString;
    @Transient
    private boolean userAttending;

    public Event(String name, String description, Date startTime, String location, String coverUrl, Club club) {

        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.location = location;
        this.coverUrl = coverUrl;
        this.club = club;
    }

    public static List<String> getPrivacyAsList() {
        ArrayList<String> list = new ArrayList<>();

        for (Privacy privacy : Privacy.values()) {
            list.add(privacy.name());
        }
        return list;
    }

    public String getTimeString() {
        if(timeString == null) {
            return setTimeString();
        }
        return timeString;
    }

    private String setTimeString() {
        DateTime dateTime = new DateTime(startTime.getTime());
        String[] splits = dateTime.toString().split("T");
        return timeString = splits[1].substring(0, 5);
    }

    public boolean isUserAttending() {
        return userAttending;
    }

    public void setUserAttending(boolean userAttending) {
        this.userAttending = userAttending;
    }

    public String getDateString() {
        if(dateString == null) {
            return setDateString();
        }
        return dateString;
    }

    private String setDateString() {
        DateTime dateTime = new DateTime(this.startTime.getTime());
        String[] splits = dateTime.toString().split("T");
        return dateString = splits[0];
    }

    public static enum Privacy {
        OPEN, MEMBERS
    }
}
