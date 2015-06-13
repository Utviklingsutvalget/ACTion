package models.composite;

import javax.persistence.Embeddable;

/**
 * The embeddable key used to arrange the relations in this model.
 *
 * @see com.avaje.ebean.Ebean
 */
@Embeddable
public class ActivationKey {

    private Long powerupId;
    private Long clubId;

    public ActivationKey() {

    }

    public ActivationKey(Long clubId, Long powerupId) {
        this.clubId = clubId;
        this.powerupId = powerupId;
    }

    public Long getClubId() {
        return clubId;
    }

    public void setClubId(final Long clubId) {
        this.clubId = clubId;
    }

    public Long getPowerupId() {
        return powerupId;
    }

    public void setPowerupId(final Long powerupId) {
        this.powerupId = powerupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActivationKey that = (ActivationKey) o;

        return getClubId().equals(that.getClubId()) && getPowerupId().equals(that.getPowerupId());

    }

    @Override
    public int hashCode() {
        int result = getPowerupId().hashCode();
        result = 31 * result + getClubId().hashCode();
        return result;
    }
}
