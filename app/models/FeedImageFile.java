package models;

import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class FeedImageFile extends Model {

    private static final String FEEDCOL = "feed_id";
    private static final String IMAGECOLUMN = "image_path";
    private static final String FILENAMECOLUMN = "filename";

    public static Finder<Long, FeedImageFile> find = new Finder<>(Long.class, FeedImageFile.class);

    public static FeedImageFile findPathByFeed(Feed feed){
        return find.where().eq(FEEDCOL, feed.id).findUnique();
    }

    public FeedImageFile(Feed feed, String path, String fileName) {
        this.feed = feed;
        this.path = path;
        this.fileName = fileName;
    }

    @Id
    public Long id;

    @OneToOne
    @Column(name = FEEDCOL)
    public Feed feed;

    @Column(name = IMAGECOLUMN)
    public String path;

    @Column(name = FILENAMECOLUMN)
    public String fileName;

    @Override
    public String toString() {
        return "FeedImageFile{" +
                "id=" + id +
                ", feed=" + feed +
                ", path='" + path + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
