package services;

import com.avaje.ebean.Ebean;
import models.Location;

import java.util.List;

public class LocationService {
    public List<Location> findAll() {
        return Ebean.find(Location.class).findList();
    }

    public Location findById(final Long locationId) {
        return Ebean.find(Location.class).setId(locationId).findUnique();
    }
}
