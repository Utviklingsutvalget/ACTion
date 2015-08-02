package services;

import com.avaje.ebean.Ebean;
import models.InitiationEvent;

import java.util.List;

public class InitiationEventService {
    public List<InitiationEvent> findAll() {
        return Ebean.find(InitiationEvent.class).findList();
    }

    public void save(final InitiationEvent initiationEvent) {
        Ebean.save(initiationEvent);
    }

    public void delete(long initiationEventId) {
        Ebean.delete(InitiationEvent.class, initiationEventId);
    }
}
