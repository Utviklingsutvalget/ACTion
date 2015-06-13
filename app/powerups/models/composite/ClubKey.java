package powerups.models.composite;

import javax.persistence.Embeddable;

@Embeddable
public class ClubKey {

    private Long clubId;

    public ClubKey(Long clubId) {
        this.clubId = clubId;
    }

    public Long getClubId() {
        return clubId;
    }

    public void setClubId(final Long clubId) {
        this.clubId = clubId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClubKey that = (ClubKey) o;

        return clubId.equals(that.clubId);

    }

    @Override
    public int hashCode() {
        return clubId.hashCode();
    }
}