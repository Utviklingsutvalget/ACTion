package models;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
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
    public DateTime startTime;
    @Constraints.Required
    public DateTime endTime;
    @Constraints.Required
    public String location;
    public String coverUrl;
    @ManyToOne
    public User host;
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

    public Event(String name, String description, DateTime startTime, String location, String coverUrl, Club club, User host) {

        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.location = location;
        this.coverUrl = coverUrl;
        this.club = club;
        this.host = host;
    }

    public User getHost(){
        return this.host;
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
        DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");
        return timeString = fmt.print(startTime.toInstant());
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
        DateTimeFormatter fmt = DateTimeFormat.forPattern("d. MMMM");
        return dateString = fmt.print(startTime.toInstant());
    }

    public static enum Privacy {
        OPEN, MEMBERS
    }
}
