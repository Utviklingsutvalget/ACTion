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
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    @Column(name = CLUBCOLUMN)
    private Club club;
    @play.data.validation.Constraints.Required
    @Column(length = 1500)
    private String message;
    @play.data.validation.Constraints.Required
    private String messageTitle;
    //length limiatation to be reviewed
    private String pictureUrl;

    public static String getCLUBCOLUMN() {
        return CLUBCOLUMN;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(final Club club) {
        this.club = club;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(final String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(final String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

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
