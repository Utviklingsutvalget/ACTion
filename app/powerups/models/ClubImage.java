package powerups.models;

import models.Club;

import javax.persistence.*;

@Entity
public class ClubImage {

    @EmbeddedId
    private ClubImageKey key;
    @OneToOne
    @JoinColumn(name = "club_id", nullable = true, insertable = false, updatable = false)
    private Club club;
    private String imageUrl;

    public ClubImage(Club club, String imageUrl) {
        this.club = club;
        this.imageUrl = imageUrl;
        this.key = new ClubImageKey(this.club.getId());
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

    public ClubImageKey getKey() {
        return key;
    }

    public void setKey(final ClubImageKey key) {
        this.key = key;
    }

    @Embeddable
    public class ClubImageKey {

        private Long clubId;

        public ClubImageKey(Long clubId) {
            this.clubId = clubId;
        }

        public Long getClubId() {
            return clubId;
        }

        public void setClubId(final Long clubId) {
            this.clubId = clubId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ClubImageKey that = (ClubImageKey) o;

            return clubId.equals(that.clubId);

        }

        @Override
        public int hashCode() {
            return clubId.hashCode();
        }
    }
}
