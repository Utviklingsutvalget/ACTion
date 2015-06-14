package models;

import com.feth.play.module.pa.user.AuthUser;
import com.feth.play.module.pa.user.EmailIdentity;
import com.feth.play.module.pa.user.FirstLastNameIdentity;
import com.feth.play.module.pa.user.PicturedIdentity;
import org.hibernate.validator.constraints.Email;
import services.UserService;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "user")
    private List<Membership> memberships = new ArrayList<>();
    @Id
    private String id;
    private String firstName;
    private String lastName;
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
        this.email = email;
        this.pictureUrl = picureUrl;
    }

    public User(final AuthUser authUser) {
        this.id = authUser.getId();
        if (authUser instanceof EmailIdentity) {
            EmailIdentity identity = (EmailIdentity) authUser;
            this.email = identity.getEmail();
        }
        if(authUser instanceof FirstLastNameIdentity) {
            FirstLastNameIdentity identity = (FirstLastNameIdentity) authUser;
            this.firstName = identity.getFirstName();
            this.lastName = identity.getLastName();
        }
        if(authUser instanceof PicturedIdentity) {
            PicturedIdentity identity = (PicturedIdentity) authUser;
            this.pictureUrl = identity.getPicture();
        }
    }

    public static List<String> genderAsList() {
        ArrayList<String> list = new ArrayList<>();

        for (Gender gender : Gender.values()) {
            list.add(gender.toString());
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

    private String getGravatarUrl() {
        return gravatarUrl;
    }

    public void setGravatarUrl(String gravatarUrl) {
        this.gravatarUrl = gravatarUrl;
    }

    public enum Gender {
        MALE, FEMALE, UNSPECIFIED;

        @Override
        public String toString() {
            switch (this) {
                case MALE:
                    return "Mann";
                case FEMALE:
                    return "Kvinne";
                case UNSPECIFIED:
                    return "Ukjent/Uspesifisert";
                default:
                    return "Ukjent/Uspesifisert";
            }
        }
    }
}
