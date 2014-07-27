package models;

import org.joda.time.DateTime;
import play.db.ebean.Model;
import javax.persistence.*;
import java.util.List;

@Entity
public class Feed extends Model {

    public static Finder<Long, Feed> find = new Finder<>(Long.class, Feed.class);

    private static final String CLUBCOLUMN = "club";

    public static List<Feed> findByClub(Club club){
        return find.where().eq(CLUBCOLUMN, club).findList();
    }

    public Feed(Club club, String message, String messageTitle){
        this.club = club;
        this.message = message;
        this.messageTitle = messageTitle;
        this.dateTime = new DateTime();
    }

    @Id
    public Long id;

    @ManyToOne
    public User user;

    @ManyToOne
    @Column(name = CLUBCOLUMN)
    public Club club;

    @play.data.validation.Constraints.Required
    public String message;

    @play.data.validation.Constraints.Required
    public String messageTitle;

    @Column(name = "Created")
    public DateTime dateTime;

    @PrePersist
    void onCreate(){
        this.dateTime = new DateTime();
    }

    @Override
    public String toString() {
        return "Feed{" +
                "id=" + id +
                ", club=" + club +
                ", message='" + message + '\'' +
                ", messageTitle='" + messageTitle + '\'' +
                ", dateTime=" + dateTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Feed)) return false;
        if (!super.equals(o)) return false;

        Feed feed = (Feed) o;

        if (!id.equals(feed.id)) return false;

        return true;
    }
}
