package models.composite;

import models.Club;
import models.User;

import javax.persistence.Embeddable;

@Embeddable
public class ClubUserKey {

    private Long clubId;

    private String userId;

    public ClubUserKey(Club club, User user) {
        this.clubId = club.getId();
        this.userId = user.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClubUserKey that = (ClubUserKey) o;

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
