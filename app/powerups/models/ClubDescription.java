package powerups.models;

import models.clubs.Club;
import play.data.validation.Constraints;
import powerups.models.composite.ClubKey;

import javax.persistence.*;

@Entity
@Table(name = "pl_ClubDescription")
public class ClubDescription {

    @EmbeddedId
    private ClubKey key;
    @OneToOne
    @JoinColumn(name = "club_id", nullable = true, insertable = false, updatable = false)
    private Club club;

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

    public ClubKey getKey() {
        return key;
    }

    public void setKey(final ClubKey key) {
        this.key = key;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(final Club club) {
        this.club = club;
        this.key.setClubId(club.getId());
    }
}
