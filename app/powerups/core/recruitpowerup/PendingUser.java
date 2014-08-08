package powerups.core.recruitpowerup;

import models.Club;
import models.User;

public class PendingUser {

    public String userID;
    public Long clubId;

    public PendingUser(User user, Club club) {
        this.clubId = club.id;
        this.userID = user.getId();
    }

    public String getUserID() {
        return this.userID;
    }

    public Long getClubId() {
        return this.clubId;
    }
}
