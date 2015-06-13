package models;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import models.composite.InitiationKey;
import play.data.validation.Constraints;

import javax.persistence.*;

@Entity
public class InitiationGroup {

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

        this.id = new InitiationKey(guardian.getId(), location.getId());
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

}
