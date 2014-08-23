package models;

import play.api.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.List;

@Entity
public class UserImageFile extends Model {

    private static final String USERCOL = "user_id";
    private static final String IMAGECOLUMN = "image_path";
    private static final String FILENAMECOLUMN = "filename";

    public static Finder<Long, UserImageFile> find = new Finder<>(Long.class, UserImageFile.class);

    public UserImageFile(User user, String imagePath, String fileName) {
        this.user = user;
        this.imagePath = imagePath;
        this.fileName = fileName;
    }

    @Id
    public Long id;

    @ManyToOne
    @Column(name = USERCOL)
    public User user;

    @play.data.validation.Constraints.Required
    @Column(name = IMAGECOLUMN)
    public String imagePath;

    @play.data.validation.Constraints.Required
    @Column(name = FILENAMECOLUMN)
    public String fileName;

    public static List<UserImageFile> findImagesByUser(User user){
        return find.where().eq(USERCOL, user.getId()).findList();
    }
}
