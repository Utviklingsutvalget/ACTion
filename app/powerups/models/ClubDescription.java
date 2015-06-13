package powerups.models;

import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "pl_ClubDescription")
public class ClubDescription {

    @Id
    @OneToOne
    private Long clubId;

    @Constraints.Required
    @Constraints.MaxLength(10000)
    private String description;
    @Constraints.Required
    @Constraints.MaxLength(300)
    private String listDescription;

    public String getListDescription() {
        return listDescription;
    }

    public void setListDescription(final String listDescription) {
        this.listDescription = listDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Long getClubId() {
        return clubId;
    }

    public void setClubId(final Long clubId) {
        this.clubId = clubId;
    }

}
