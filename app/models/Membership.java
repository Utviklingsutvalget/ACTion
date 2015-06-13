package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.annotation.Transactional;
import play.data.validation.Constraints;
import utils.MembershipLevel;

import javax.persistence.*;

@Entity
public class Membership {

    @EmbeddedId
    private MembershipKey id;
    @ManyToOne
    @JoinColumn(name = "club_id", insertable = false, updatable = false)
    private Club club;
    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
    @Constraints.Required
    private MembershipLevel level;

    public Membership(Club club, User user) {
        this(club, user, MembershipLevel.MEMBER);
    }

    public Membership(Club club, User user, MembershipLevel level) {
        this.club = club;
        this.user = user;
        this.level = level;
        this.id = new MembershipKey(club, user);
    }

    @Transactional
    public static void update(Membership membership) {
        Ebean.save(membership);
    }

    public MembershipKey getId() {
        return id;
    }

    public void setId(final MembershipKey id) {
        this.id = id;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(final Club club) {
        this.club = club;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public MembershipLevel getLevel() {
        return level;
    }

    public void setLevel(final MembershipLevel level) {
        this.level = level;
    }

    @Embeddable
    public class MembershipKey {

        private Long clubId;

        private String userId;

        public MembershipKey(Club club, User user) {
            this.clubId = club.getId();
            this.userId = user.getId();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MembershipKey that = (MembershipKey) o;

            return clubId.equals(that.clubId) && userId.equals(that.userId);

        }

        @Override
        public int hashCode() {
            int result = clubId.hashCode();
            result = 31 * result + userId.hashCode();
            return result;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(final String userId) {
            this.userId = userId;
        }

        public Long getClubId() {
            return clubId;
        }

        public void setClubId(final Long clubId) {
            this.clubId = clubId;
        }
    }

}
