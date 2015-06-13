package powerups.models;

import models.Club;
import powerups.models.composite.ClubKey;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class ClubImage {

    @EmbeddedId
    private ClubKey key;
    @OneToOne
    @JoinColumn(name = "club_id", nullable = true, insertable = false, updatable = false)
    private Club club;
    private String imageUrl;

    public ClubImage(Club club, String imageUrl) {
        this.club = club;
        this.imageUrl = imageUrl;
        this.key = new ClubKey(this.club.getId());
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(final String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(final Club club) {
        this.club = club;
    }

    public ClubKey getKey() {
        return key;
    }

    public void setKey(final ClubKey key) {
        this.key = key;
    }

}
