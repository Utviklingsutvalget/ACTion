package powerups.models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.annotation.Transactional;
import models.Club;
import models.User;
import play.data.validation.Constraints;

import javax.persistence.*;

@Entity
public class Pending {

    @EmbeddedId
    private PendingKey key;

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
        this.key = new PendingKey(user, club);
    }

    public Pending(Club club, User user, String applicationMessage) {
        this.user = user;
        this.club = club;
        this.applicationMessage = applicationMessage;
        this.key = new PendingKey(user, club);
    }

    @Transactional
    public static void update(Pending pending) {
        Ebean.save(pending);
    }

    public PendingKey getKey() {
        return key;
    }

    public void setKey(final PendingKey key) {
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

    @Embeddable
    public class PendingKey {

        private String userId;
        private Long clubId;

        public PendingKey(User user, Club club) {
            this.userId = user.getId();
            this.clubId = club.getId();
        }

        public Long getClubId() {
            return clubId;
        }

        public void setClubId(final Long clubId) {
            this.clubId = clubId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(final String userId) {
            this.userId = userId;
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) return true;

            if (other instanceof PendingKey) {

                PendingKey p = (PendingKey) other;

                return p.clubId.equals(this.clubId) && p.userId.equals(this.userId);

            } else {
                return false;
            }
        }
    }

}
