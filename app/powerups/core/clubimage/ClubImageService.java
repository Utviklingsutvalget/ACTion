package powerups.core.clubimage;

import com.avaje.ebean.Ebean;
import models.Club;
import powerups.models.ClubImage;

public class ClubImageService {
    public ClubImage getImageByClub(Club club) {
        return Ebean.find(ClubImage.class).where().eq("club_id", club.getId()).findUnique();
    }

    public ClubImage findById(final ClubImage.ClubImageKey key) {
        return Ebean.find(ClubImage.class).setId(key).findUnique();
    }
}
