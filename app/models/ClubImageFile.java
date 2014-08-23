package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class ClubImageFile extends Model {

    private static final String CLUBCOLUMN = "club_id";
    private static final String IMAGECOLUMN = "image_path";
    private static final String FILENAMECOLUMN = "filename";

    public static Finder<Long, ClubImageFile> find = new Finder<>(Long.class, ClubImageFile.class);

    public ClubImageFile(Club club, String path, String fileName){
        this.club = club;
        this.path = path;
        this.fileName = fileName;
    }

    public static ClubImageFile findPathByClub(Club club){
        return find.where().eq(CLUBCOLUMN, club.id).findUnique();
    }

    @Id
    public Long id;

    @OneToOne
    @Column(name = CLUBCOLUMN)
    public Club club;

    @Constraints.Required
    @Column(name = IMAGECOLUMN)
    public String path;

    @Column(name = FILENAMECOLUMN)
    public String fileName;

    @Override
    public String toString() {
        return "ClubImageFile{" +
                "id=" + id +
                ", club=" + club +
                ", path='" + path + '\'' +
                '}';
    }

}
