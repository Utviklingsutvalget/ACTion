package services;

import com.avaje.ebean.Ebean;
import models.InitiationSchedule;

import java.util.List;

public class InitiationScheduleService {
    public List<InitiationSchedule> findAll() {
        return Ebean.find(InitiationSchedule.class).findList();
    }

    public void save(final InitiationSchedule initiationSchedule) {
        Ebean.save(initiationSchedule);
    }


    public InitiationSchedule findScheduleById(long scheduleId) {
        return Ebean.find(InitiationSchedule.class, scheduleId);
    }
}
