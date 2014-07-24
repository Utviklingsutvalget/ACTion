package powerups.models;

import models.Club;
import models.User;
import play.db.ebean.Model;

import javax.persistence.*;

@Entity
public class BoardMembership extends Model {

    public static Finder<BoardMembershipKey, BoardMembership> find = new Finder<>(BoardMembershipKey.class, BoardMembership.class);

    public BoardMembership(Club club, BoardPost boardPost, User user){
        this.BoardPost = boardPost;
        this.club = club;
        this.user = user;
        this.weight = boardPost.weight;
        this.key = new BoardMembershipKey(club, boardPost);
    }

    @EmbeddedId
    public BoardMembershipKey key;

    @ManyToOne
    @JoinColumn(name = "club_id", insertable = false, updatable = false)
    public Club club;

    @ManyToOne
    @JoinColumn(name = "board_post_id", insertable = false, updatable = false)
    public BoardPost BoardPost;

    @OneToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    public User user;

    public int weight;

    @Embeddable
    public class BoardMembershipKey{

        public Long clubId;
        public Long BoardPostId;

        public BoardMembershipKey(Club club, BoardPost boardPost){

            this.clubId = club.id;
            this.BoardPostId = boardPost.id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            BoardMembershipKey that = (BoardMembershipKey) o;

            if (!BoardPostId.equals(that.BoardPostId)) return false;
            if (!clubId.equals(that.clubId)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = clubId.hashCode();
            result = 31 * result + BoardPostId.hashCode();
            return result;
        }
    }

}
