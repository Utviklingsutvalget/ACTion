package powerups.models;


import models.Club;
import models.Membership;
import models.User;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Board extends Model {
    public static Finder<Long, Board> find = new Finder<>(Long.class, Board.class);

    @Id
    @OneToOne
    public Long clubID;

    @OneToOne
    @PrimaryKeyJoinColumn
    public Membership leader;

    @OneToOne
    @PrimaryKeyJoinColumn
    public Membership vice;

    @OneToOne
    @PrimaryKeyJoinColumn
    public Membership economy;

    @OneToMany(mappedBy = "board")
    public List<BoardExtras> boardExtra;

}
