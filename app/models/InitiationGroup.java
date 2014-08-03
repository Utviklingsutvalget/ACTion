package models;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import javax.persistence.*;

@Entity
public class InitiationGroup {

    @EmbeddedId
    private InitiationKey id;

    public InitiationGroup(User guardian, Location location, int groupNumber) {
        this.guardian = guardian;
        this.location = location;

        this.id = new InitiationKey(groupNumber, guardian.id, location.id);
    }

    @OneToOne
    private User guardian;

    @ManyToOne
    private Location location;

    private String phoneNumber;

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

    public Phonenumber.PhoneNumber getPhoneNumber() {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber phoneNumber = null;
        try {
            phoneNumber = phoneUtil.parse(this.phoneNumber, "NO");
        } catch (NumberParseException e) {
            e.printStackTrace();
        }
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber phoneNo = null;
        try {
            phoneNo = phoneUtil.parse(phoneNumber, "NO");
        } catch (NumberParseException e) {
            e.printStackTrace();
        }
        if(phoneUtil.isValidNumberForRegion(phoneNo, "NO")) {
            this.phoneNumber = phoneNumber;
        }
    }

    @Embeddable
    public class InitiationKey {

        private int groupNumber;
        private String guardianId;
        private Long locationId;

        public InitiationKey(int groupNumber, String userId, Long locationId) {

            this.groupNumber = groupNumber;
            this.guardianId = userId;
            this.locationId = locationId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            InitiationKey that = (InitiationKey) o;

            return groupNumber == that.groupNumber && locationId.equals(that.locationId) && guardianId.equals(that.guardianId);

        }

        @Override
        public int hashCode() {
            int result = groupNumber;
            result = 31 * result + guardianId.hashCode();
            result = 31 * result + locationId.hashCode();
            return result;
        }

        public Long getLocationId() {
            return locationId;

        }

        public void setLocationId(Long locationId) {
            this.locationId = locationId;
        }

        public String getGuardianId() {
            return guardianId;
        }

        public void setGuardianId(String guardianId) {
            this.guardianId = guardianId;
        }

        public int getGroupNumber() {
            return groupNumber;
        }

        public void setGroupNumber(int groupNumber) {
            this.groupNumber = groupNumber;
        }


    }
}
