package models;

import models.composite.ClubUserKey;
import play.data.validation.Constraints;
import utils.MembershipLevel;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Membership {

    @EmbeddedId
    private ClubUserKey id;
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
        this.id = new ClubUserKey(club, user);
    }

    public ClubUserKey getId() {
        return id;
    }

    public void setId(final ClubUserKey id) {
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

}
