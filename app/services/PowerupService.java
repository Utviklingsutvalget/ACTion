package services;

import com.avaje.ebean.Ebean;
import models.PowerupModel;

import java.util.List;

public class PowerupService {
    public List<PowerupModel> findAllMandatory() {
        return Ebean.find(PowerupModel.class).where().eq("is_mandatory", true).findList();
    }
}
