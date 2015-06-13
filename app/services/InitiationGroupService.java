package services;

import com.avaje.ebean.Ebean;
import models.InitiationGroup;
import models.composite.InitiationKey;

import java.util.List;

public class InitiationGroupService {
    public List<InitiationGroup> findAll() {
        return Ebean.find(InitiationGroup.class).findList();
    }

    public InitiationGroup findById(final InitiationKey id) {
        return Ebean.find(InitiationGroup.class)
                .where()
                .eq("guardian_id", id.getGuardianId())
                .eq("location_id", id.getLocationId())
                .findUnique();
    }
}
