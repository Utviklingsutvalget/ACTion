package powerups.models;

import models.Club;
import models.UploadedFile;
import play.db.ebean.Model;

import javax.persistence.*;

@Entity
public class ClubImage extends Model {
    public static Finder<ClubImageKey, ClubImage> find = new Finder<>(ClubImageKey.class, ClubImage.class);

    @EmbeddedId
    public ClubImageKey key;

    public ClubImage(Club club, String imageUrl) {
        this.club = club;
        this.imageUrl = imageUrl;
        this.key = new ClubImageKey(this.club.id);
    }

    public static ClubImage getImageByClub(Club club){
        return find.where().eq("club_id", club.id).findUnique();
    }

    @OneToOne
    @JoinColumn(name = "club_id", nullable = true, insertable = false, updatable = false)
    public Club club;

    public String imageUrl;

    @Embeddable
    public class ClubImageKey {

        public Long clubId;

        public ClubImageKey(Long clubId) {
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
