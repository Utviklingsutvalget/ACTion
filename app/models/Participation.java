package models;

import com.avaje.ebean.Ebean;
import play.db.ebean.Model;
import play.db.ebean.Transactional;

import javax.persistence.*;

@Entity
public class Participation extends Model {

    public static Finder<Long, Participation> find = new Finder<>(Long.class, Participation.class);

    public Participation(Event event, User user){
        this.event = event;
        this.user = user;
        this.key = new ParticipationKey(event, user);
    }

    @EmbeddedId
    public ParticipationKey key;

    @ManyToOne
    public User user;

    @ManyToOne
    public Event event;

    @Transactional
    public static void save(Participation uie) {

        if(!exists(uie.event.id, uie.user.id))
            Ebean.save(uie);
    }

    public static boolean exists(Long eventId, String userId) {

        return find
                .where().eq("event_id", eventId)
                .where().eq("user_id", userId)
                .findRowCount() > 0;

    }

    public static boolean userIsParticipatingEvent(Long event_id, String user_id) {

        return find
                .fetch("event")
                .fetch("user")
                .where().eq("event.id", event_id)
                .where().eq("user.id", user_id)
                .findRowCount() > 0;
    }

    @Embeddable
    public class ParticipationKey {

        public Long eventId;

        public String userId;

        public ParticipationKey(final Event event, final User user) {
            this.eventId = event.id;
            this.userId = user.id;
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