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
        this.id = new ParticipationKey(event.id, user.id);
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

    public void setRvsp(boolean newRvsp) {
        if(newRvsp) {
            this.rvsp = Status.ATTENDING;
        } else {
            this.rvsp = Status.NOT_ATTENDING;
        }
    }

    public boolean getRvsp() {
        return rvsp == Status.ATTENDING;
    }

    public enum Status {
        NOT_ATTENDING, ATTENDING
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