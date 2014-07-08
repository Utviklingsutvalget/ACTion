package powerups.models;

import models.UploadedFile;
import play.db.ebean.Model;

import javax.persistence.Id;

public class ClubImage extends Model {
    public static Finder<Long, ClubImage> find = new Finder<>(Long.class, ClubImage.class);

    @Id
    public Long clubId;

    public UploadedFile file;

}
