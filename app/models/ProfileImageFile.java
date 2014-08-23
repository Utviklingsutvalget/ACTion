package models;

import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.List;

@Entity
public class ProfileImageFile extends Model {

    private static final String USERCOLUMN = "user_id";
    private static final String IMAGECOLUMN = "image_path";

    public static Finder<Long, ProfileImageFile> find = new Finder<>(Long.class, ProfileImageFile.class);

    public ProfileImageFile(User user, String path) {
        this.user = user;
        this.path = path;
    }

    @Id
    public Long id;

    @ManyToOne
    @Column(name = USERCOLUMN)
    public User user;

    @Column(name = IMAGECOLUMN)
    public String path;

    public List<ProfileImageFile> getUserProfileImages(User user){
        return find.where().eq(USERCOLUMN, user).findList();
    }

    @Override
    public String toString() {
        return "ProfileImageFile{" +
                "id=" + id +
                ", user=" + user +
                ", path='" + path + '\'' +
                '}';
    }
}
