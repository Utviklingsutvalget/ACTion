package services;

import com.avaje.ebean.Ebean;
import models.InitiationGroup;

import java.util.List;

public class InitiationGroupService {
    public List<InitiationGroup> findAll() {
        return Ebean.find(InitiationGroup.class).findList();
    }
}
