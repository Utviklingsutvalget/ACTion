package models.composite;

import javax.persistence.Embeddable;

@Embeddable
public class InitiationKey {

    private String guardianId;
    private Long locationId;

    public InitiationKey(String userId, Long locationId) {

        this.guardianId = userId;
        this.locationId = locationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InitiationKey that = (InitiationKey) o;

        return guardianId.equals(that.guardianId) && locationId.equals(that.locationId);

    }

    @Override
    public int hashCode() {
        int result = guardianId.hashCode();
        result = 31 * result + locationId.hashCode();
        return result;
    }

    public String getGuardianId() {
        return guardianId;
    }

    public void setGuardianId(String guardianId) {
        this.guardianId = guardianId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }
}
