package powerups.models;

import models.User;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;


@Entity
public class BoardExtras extends Model {
    public static Finder<Long, BoardExtras> find = new Finder<>(Long.class, BoardExtras.class);

    public static final String BOARD_COLUMN = "board_club_id";
    public static final String MEMBER_COLUMN = "member_id";
    public static final String TITLE_COLUMN = "title";

    @Id
    public Long boardExtraId;

    @ManyToOne
    @Column(name = BOARD_COLUMN)
    public Board board;

    @OneToOne
    @PrimaryKeyJoinColumn
    @Column(name = MEMBER_COLUMN)
    public User member;

    @Column(name = TITLE_COLUMN)
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
        return find.where().eq(BOARD_COLUMN, board.club.id).eq(TITLE_COLUMN, title).findList();
    }



}
