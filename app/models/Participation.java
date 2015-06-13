package models;

import play.data.validation.Constraints;

import javax.persistence.*;

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
            this.id = new ParticipationKey(event.id, user.getId());

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

    @Embeddable
    public class ParticipationKey {

        private Long eventId;
        private String userId;

        public ParticipationKey(final Long eventId, final String userId) {
            this.eventId = eventId;
            this.userId = userId;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            final ParticipationKey that = (ParticipationKey) o;

            return eventId.equals(that.eventId) && userId.equals(that.userId);

        }

        @Override
        public int hashCode() {
            int result = eventId.hashCode();
            result = 31 * result + userId.hashCode();
            return result;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(final String userId) {
            this.userId = userId;
        }

        public Long getEventId() {
            return eventId;
        }

        public void setEventId(final Long eventId) {
            this.eventId = eventId;
        }
    }
}