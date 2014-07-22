package powerups.models;

import models.Club;
import models.Membership;
import models.User;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;


@Entity
public class BoardExtras extends Model {

    public static Finder<Long, BoardExtras> find = new Finder<>(Long.class, BoardExtras.class);
    public static final String BOARDCOLUMNSTRING = "board_club_id";
    public static final String MEMBERCOLUMNSTRING = "member_id";
    public static final String TITLECOLUMNSTRING = "title";

    @Id
    public Long boardExtraId;

    @ManyToOne
    @Column(name = BOARDCOLUMNSTRING)
    public Board board;

    @Constraints.Required
    @OneToOne
    @PrimaryKeyJoinColumn
    @Column(name = MEMBERCOLUMNSTRING)
    public User member;

    @Constraints.Required
    @Column(name = TITLECOLUMNSTRING)
    public String title;

    public BoardExtras(User user, String title){
        this.member = user;
        this.title = title;
    }

    public void setTitle(String title, User user){
        this.title = title;
        this.member = user;
    }

    public static List<BoardExtras> findTitlesByBoard(Board board, String title){
        return find.where().eq(BOARDCOLUMNSTRING, board.club.id).eq(TITLECOLUMNSTRING, title).findList();
    }



}
