package services;

import com.avaje.ebean.Ebean;
import models.clubs.Club;
import models.Location;

import java.util.List;

public class ClubService {
    public Club findById(final Long id) {
        return Ebean.find(Club.class).setId(id).findUnique();
    }

    public List<Club> findAll() {
        return Ebean.find(Club.class).findList();
    }

    public List<Club> findByLocation(final Location location) {
        return Ebean.find(Club.class).where().eq("location", location).findList();
    }

    public void save(final Club club) {
        Ebean.save(club);
    }

    public void deleteClub(final Club club) {
        Ebean.delete(club);
    }
}
