package powerups.core.descriptionpowerup;

import com.avaje.ebean.Ebean;
import powerups.models.ClubDescription;

public class DescriptionService {
    public ClubDescription findByClubId(final Long id) {
        return Ebean.find(ClubDescription.class).where().eq("club_id", id).findUnique();
    }
}
