package models;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.persistence.*;
import java.util.Locale;

@Entity
public class Feed {

    private static final String CLUBCOLUMN = "club";
    @Id
    public Long id;
    @ManyToOne
    public User user;
    @ManyToOne
    @Column(name = CLUBCOLUMN)
    public Club club;
    @play.data.validation.Constraints.Required
    @Column(length = 1500)
    public String message;
    @play.data.validation.Constraints.Required
    public String messageTitle;
    //length limiatation to be reviewed
    public String pictureUrl;
    @Column(name = "Created")
    private DateTime dateTime;

    public Feed(Club club, User user, String messageTitle, String message, String pictureUrl) {
        this.club = club;
        this.user = user;
        this.message = message;
        this.messageTitle = messageTitle;
        this.pictureUrl = pictureUrl;
        this.dateTime = new DateTime();
    }

    public String getDateTime() {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("d. MMMM, HH:mm").withLocale(new Locale("nb", "no"));
        return fmt.print(dateTime);
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    @PrePersist
    void onCreate() {
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

        return id.equals(feed.id);

    }
}
