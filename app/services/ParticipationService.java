package services;

import com.avaje.ebean.Ebean;
import models.Participation;

public class ParticipationService {
    public Participation findById(final Participation.ParticipationKey id) {
        return Ebean.find(Participation.class)
                .where()
                .eq("user_id", id.getUserId())
                .eq("event_id", id.getEventId())
                .findUnique();
    }
}
