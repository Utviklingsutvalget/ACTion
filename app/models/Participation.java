package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;

@Entity
public class Participation extends Model {

    public static Finder<ParticipationKey, Participation> find = new Finder<>(ParticipationKey.class, Participation.class);

    public Participation(Event event, User user){
        this.event = event;
        this.user = user;
        if(user != null && event != null) {
            this.id = new ParticipationKey(event.id, user.id);

            if(user == event.getHost()){
                setHostRvsp();
            }
        }
    }

    @EmbeddedId
    public ParticipationKey id;

    @ManyToOne
    @JoinColumn(name = "event_id", insertable = false, updatable = false)
    public Event event;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    public User user;

    @Constraints.Required
    public Status rvsp;

    public void setHostRvsp(){
        this.rvsp = Status.HOSTING;
    }

    /**
     * Attempts to set the RVSP for a user to this participation's event. Returns false if no change was made.
     * @param newRvsp The RVSP status you wish to set.
     * @return Whether or not there was anything to change.
     */
    public boolean setRvsp(boolean newRvsp) {
        if(this.rvsp != Status.HOSTING){
            if(newRvsp && this.rvsp == Status.NOT_ATTENDING) {
                this.rvsp = Status.ATTENDING;
                return true;
            } else if(!newRvsp && this.rvsp == Status.ATTENDING) {
                this.rvsp = Status.NOT_ATTENDING;
                return true;
            }
        }
        return false;
    }

    public boolean getRvsp() {
        return rvsp == Status.ATTENDING || rvsp == Status.HOSTING;
    }

    public enum Status {
        NOT_ATTENDING, ATTENDING, HOSTING
    }

    @Embeddable
    public class ParticipationKey {

        public Long eventId;

        public String userId;

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
    }
}