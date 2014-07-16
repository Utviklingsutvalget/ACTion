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
    public Long clubID;

    @OneToOne
    public Club club;

    @OneToOne
    @PrimaryKeyJoinColumn
    public User leader;

    @OneToOne
    @PrimaryKeyJoinColumn
    public User vice;

    @OneToOne
    @PrimaryKeyJoinColumn
    public User economy;

    @OneToMany(mappedBy = "board")
    public List<BoardExtras> boardExtra;

    @Embeddable
    public class BoardKey {

        public Long clubId;

        public boolean equals(Object other) {
            return other == this || other instanceof BoardKey && ((BoardKey) other).clubId.equals(this.clubId);
        }

        public int hashCode() {
            return super.hashCode();
        }
    }
}
