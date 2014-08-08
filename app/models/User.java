package models;

import helpers.UserService;
import org.hibernate.validator.constraints.Email;
import play.db.ebean.Model;
import utils.MembershipLevel;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User extends Model {

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "user")
    private List<Membership> memberships = new ArrayList<>();
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private Gender gender;
    @Email
    private String email;
    private String pictureUrl;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "user")
    private List<Participation> participations = new ArrayList<>();
    @Transient
    private String gravatarUrl;

    public User(String id, String firstName, String lastName, Gender gender, String email, String picureUrl) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
        this.pictureUrl = picureUrl;
    }

    public static List<String> genderAsList() {
        ArrayList<String> list = new ArrayList<>();

        for (Gender gender : Gender.values()) {
            list.add(gender.name());
        }
        return list;
    }

    @PostLoad
    public void onPostLoad() {
        UserService.setupGravatar(this, this.pictureUrl);
    }

    public List<Membership> getMemberships() {
        return memberships;
    }

    public void setMemberships(List<Membership> memberships) {
        this.memberships = memberships;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPictureUrl() {
        if (gravatarUrl == null) {
            return this.pictureUrl;
        }
        return this.getGravatarUrl();
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public List<Participation> getParticipations() {
        return participations;
    }

    public void setParticipations(List<Participation> participations) {
        this.participations = participations;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        final User user = (User) o;

        return id.equalsIgnoreCase(user.id);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + id.hashCode();
        return result;
    }

    public boolean isAdmin() {
        if (SuperUser.find.all().isEmpty()) {
            return false;
        }
        SuperUser su = SuperUser.find.byId(new SuperUser(this).key);
        if (su == null) {
            return false;
        }
        if (!su.user.equals(this)) {
            for (Membership mem : this.memberships) {
                if (mem.level == MembershipLevel.COUNCIL) {
                    return true;
                }
            }
        } else {
            return true;
        }
        return false;
    }

    private String getGravatarUrl() {
        return gravatarUrl;
    }

    public void setGravatarUrl(String gravatarUrl) {
        this.gravatarUrl = gravatarUrl;
    }

    public enum Gender {
        MALE, FEMALE
    }
}
