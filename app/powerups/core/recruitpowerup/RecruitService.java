package powerups.core.recruitpowerup;

import com.avaje.ebean.Ebean;
import models.composite.ClubUserKey;
import powerups.models.Pending;

import java.util.List;

public class RecruitService {
    public Pending findById(final ClubUserKey key) {
        return Ebean.find(Pending.class).setId(key).findUnique();
    }

    public List<Pending> getByClubId(Long clubId) {
        return Ebean.find(Pending.class).where().eq("club_id", clubId).findList();
    }
}
