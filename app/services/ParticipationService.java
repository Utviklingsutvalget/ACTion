package services;

import com.avaje.ebean.Ebean;
import models.Participation;
import models.composite.ParticipationKey;

public class ParticipationService {
    public Participation findById(final ParticipationKey id) {
        return Ebean.find(Participation.class)
                .where()
                .eq("user_id", id.getUserId())
                .eq("event_id", id.getEventId())
                .findUnique();
    }
}
