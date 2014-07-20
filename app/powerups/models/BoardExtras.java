package powerups.models;

import models.Club;
import models.Membership;
import models.User;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;

@Entity
public class BoardExtras extends Model {

    public static Finder<BoardKey, BoardExtras> find = new Finder<>(BoardKey.class, BoardExtras.class);

    @EmbeddedId
    public BoardKey key;

    @ManyToOne
    public Board board;

    @Constraints.Required
    @OneToOne
    @PrimaryKeyJoinColumn
    public User member;

    public Club club;

    @Constraints.Required
    public String title;

    public BoardExtras(User user, String title, Board board){
        this.board = board;
        this.member = user;
        this.title = title;
        this.key = new BoardKey(board.clubID, user.id);
    }

    public void setTitle(String title, User user){
        this.title = title;
        this.member = user;
    }

    @Embeddable
    public class BoardKey {

        public Long clubId;
        public String extrasId;

        public BoardKey(Long clubId, String extrasId){
            this.clubId =  clubId;
            this.extrasId = extrasId;
        }

        public boolean equals(Object other) {
            return other == this || other instanceof BoardKey && ((BoardKey) other).clubId.equals(this.clubId);
        }

        public int hashCode() {
            return super.hashCode();
        }
    }

}
