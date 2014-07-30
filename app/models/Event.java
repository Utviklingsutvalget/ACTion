package models;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    public LocalDateTime startTime;
    @Constraints.Required
    public LocalDateTime endTime;
    @Constraints.Required
    public String location;
    public String coverUrl;
    @ManyToOne
    public User host;
    @ManyToOne
    public Club club;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "event")
    public List<Participation> participants = new ArrayList<>();
    @Transient
    private String timeString;
    @Transient
    private String dateString;
    @Transient
    private boolean userAttending;
    @Transient
    private boolean userHosting;

    public Event(String name, String description, LocalDateTime startTime, String location, String coverUrl, Club club, User host) {

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
        DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm").withLocale(new Locale("nb", "no"));
        return timeString = fmt.print(startTime);
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
        return dateString = fmt.print(startTime);
    }

    public boolean isUserHosting() {
        return userHosting;
    }

    public void setUserHosting(boolean userHosting) {
        this.userHosting = userHosting;
    }

    public static enum Privacy {
        OPEN, MEMBERS
    }
}
