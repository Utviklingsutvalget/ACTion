package services;

import com.avaje.ebean.Ebean;
import models.Event;

import java.util.List;

public class EventService {


    public List<Event> findAll() {
        return Ebean.find(Event.class).findList();
    }

    public Event findById(final Long id) {
        return Ebean.find(Event.class).setId(id).findUnique();
    }

    public void save(final Event event) {
        Ebean.save(event);
    }

    public void update(final Event event) {
        Ebean.update(event);
    }

    public void delete(final Event event) {
        Ebean.delete(event);
    }
}
