package models;

import com.avaje.ebean.Ebean;
import play.db.ebean.Model;
import play.db.ebean.Transactional;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class UsersInEvent extends Model {

    public static Finder<Long, UsersInEvent> find = new Finder<>(Long.class, UsersInEvent.class);

    @Id
    public Long id;

    @ManyToOne
    public User user;

    @ManyToOne
    public Event event;

    public UsersInEvent(Event event, User user) {

        this.event = event;
        this.user = user;
    }

    @Transactional
    public static void save(UsersInEvent uie) {

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

    public static Long getUserInEventId(Long event_id, String user_id) {

        return find
                .fetch("event")
                .fetch("user")
                .where().eq("event.id", event_id)
                .where().eq("user.id", user_id)
                .findUnique().id;
    }
}