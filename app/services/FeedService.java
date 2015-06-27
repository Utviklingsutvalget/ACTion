package services;

import com.avaje.ebean.Ebean;
import models.clubs.Club;
import models.Feed;

import java.util.List;

public class FeedService {
    public List<Feed> findAll() {
        return Ebean.find(Feed.class).findList();
    }

    public List<Feed> findByClub(final Club club) {
        return Ebean.find(Feed.class).where().eq("club", club).findList();
    }
}
