package services;

import com.avaje.ebean.Ebean;
import models.Location;

import java.util.List;
import java.util.Optional;

public class LocationService {
    public List<Location> findAll() {
        return Ebean.find(Location.class).findList();
    }

    public Location findById(final Long locationId) {
        return Ebean.find(Location.class).setId(locationId).findUnique();
    }

    public void update(final Location location) {
        Ebean.update(location);
    }

    public Location getLocationFromStringId(final List<Location> locations, final String locationId) {
        Optional<Location> first = locations.stream()
                .filter(loc -> loc.getId() == (locationId.equals("") ? 0 : Long.parseLong(locationId)))
                .findFirst();
        return first.isPresent() ? first.get() : null;
    }
}
