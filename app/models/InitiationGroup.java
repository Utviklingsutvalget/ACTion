package models;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;

@Entity
public class InitiationGroup extends Model {

    @EmbeddedId
    private InitiationKey id;
    @OneToOne
    @JoinColumn(name = "guardian_id", insertable = false, updatable = false)
    private User guardian;
    @ManyToOne
    @JoinColumn(name = "location_id", insertable = false, updatable = false)
    private Location location;
    @Constraints.Required
    private String phoneNumber;
    @Constraints.Required
    private int groupNumber;

    public InitiationGroup(User guardian, Location location, int groupNumber) {
        this.guardian = guardian;
        this.location = location;
        this.groupNumber = groupNumber;

        this.id = new InitiationKey(guardian.getId(), location.id);
    }

    public InitiationGroup(User guardian, Location location) {
        this(guardian, location, 0);
    }

    public InitiationKey getId() {
        return id;
    }

    public void setId(InitiationKey id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public User getGuardian() {
        return guardian;
    }

    public void setGuardian(User guardian) {
        this.guardian = guardian;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) throws NumberParseException {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber phoneNo;
        phoneNo = phoneUtil.parse(phoneNumber, "NO");
        if (phoneUtil.isValidNumberForRegion(phoneNo, "NO")) {
            this.phoneNumber = phoneNumber;
        }
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }

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
}
