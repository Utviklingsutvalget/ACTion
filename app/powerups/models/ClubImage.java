package powerups.models;

import models.UploadedFile;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ClubImage extends Model {
    public static Finder<Long, ClubImage> find = new Finder<>(Long.class, ClubImage.class);

    @Id
    public Long clubId;

    public UploadedFile file;

}
