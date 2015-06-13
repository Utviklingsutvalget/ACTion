package models;

import models.composite.ParticipationKey;
import play.data.validation.Constraints;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Participation {

    @EmbeddedId
    private ParticipationKey id;
    @ManyToOne
    @JoinColumn(name = "event_id", insertable = false, updatable = false)
    private Event event;
    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
    @Constraints.Required
    private Status rvsp;

    public Participation(Event event, User user) {
        this.event = event;
        this.user = user;
        if (user != null && event != null) {
            this.id = new ParticipationKey(event.getId(), user.getId());

            if (user == event.getHost()) {
                setHostRvsp();
            }
        }
    }

    public ParticipationKey getId() {
        return id;
    }

    public void setId(final ParticipationKey id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(final Event event) {
        this.event = event;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public void setHostRvsp() {
        this.rvsp = Status.HOSTING;
    }

    /**
     * Attempts to set the RVSP for a user to this participation's event. Returns false if no change was made.
     *
     * @param newRvsp The RVSP status you wish to set.
     * @return Whether or not there was anything to change.
     */
    public boolean setRvsp(boolean newRvsp) {
        if (this.rvsp != Status.HOSTING) {
            if (newRvsp && (this.rvsp == Status.NOT_ATTENDING || this.rvsp == null)) {
                this.rvsp = Status.ATTENDING;
                return true;
            } else if (!newRvsp && (this.rvsp == Status.ATTENDING || this.rvsp == null)) {
                this.rvsp = Status.NOT_ATTENDING;
                return true;
            }
        }
        return false;
    }

    public boolean getRvsp() {
        return rvsp == Status.ATTENDING || rvsp == Status.HOSTING;
    }

    public void setRvsp(final Status rvsp) {
        this.rvsp = rvsp;
    }

    public Status getRvspObject() {
        return rvsp;
    }

    public enum Status {
        NOT_ATTENDING, ATTENDING, HOSTING
    }

}
