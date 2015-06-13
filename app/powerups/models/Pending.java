package powerups.models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.annotation.Transactional;
import models.Club;
import models.User;
import models.composite.ClubUserKey;
import play.data.validation.Constraints;

import javax.persistence.*;

@Entity
public class Pending {

    @EmbeddedId
    private ClubUserKey key;

    @Constraints.Required
    @Constraints.MaxLength(100)
    @Column(length = 100)
    private String applicationMessage;

    @ManyToOne
    @JoinColumn(name = "club_id", insertable = false, updatable = false)
    private Club club;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    public Pending(Club club, User user) {
        this.user = user;
        this.club = club;
        this.key = new ClubUserKey(club, user);
    }

    public Pending(Club club, User user, String applicationMessage) {
        this.user = user;
        this.club = club;
        this.applicationMessage = applicationMessage;
        this.key = new ClubUserKey(club, user);
    }

    @Transactional
    public static void update(Pending pending) {
        Ebean.save(pending);
    }

    public ClubUserKey getKey() {
        return key;
    }

    public void setKey(final ClubUserKey key) {
        this.key = key;
    }

    public String getApplicationMessage() {
        return applicationMessage;
    }

    public void setApplicationMessage(final String applicationMessage) {
        this.applicationMessage = applicationMessage;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(final Club club) {
        this.club = club;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

}
