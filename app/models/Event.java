package models;

import models.clubs.Club;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Entity
public class Event {

    @Id
    private Long id;
    @Constraints.Required
    private Privacy privacy;
    @Constraints.Required
    private String name;
    @Constraints.Required
    @Column(length = 3000)
    private String description;
    @Constraints.Required
    private LocalDateTime startTime;
    @Constraints.Required
    private LocalDateTime endTime;
    @Constraints.Required
    private String location;
    private String coverUrl;
    @ManyToOne
    private User host;
    @ManyToOne
    private Club club;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "event")
    private List<Participation> participants = new ArrayList<>();
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

    public static List<String> getPrivacyAsList() {
        ArrayList<String> list = new ArrayList<>();

        for (Privacy privacy : Privacy.values()) {
            list.add(privacy.name());
        }
        return list;
    }

    public List<Participation> getParticipants() {
        return participants;
    }

    public void setParticipants(final List<Participation> participants) {
        this.participants = participants;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(final Club club) {
        this.club = club;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(final String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(final String location) {
        this.location = location;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(final LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(final LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Privacy getPrivacy() {
        return privacy;
    }

    public void setPrivacy(final Privacy privacy) {
        this.privacy = privacy;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public User getHost() {
        return this.host;
    }

    public void setHost(final User host) {
        this.host = host;
    }

    public String getTimeString() {
        if (timeString == null) {
            return setTimeString();
        }
        return timeString;
    }

    public void setTimeString(final String timeString) {
        this.timeString = timeString;
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
        if (dateString == null) {
            return setDateString();
        }
        return dateString;
    }

    public void setDateString(final String dateString) {
        this.dateString = dateString;
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
