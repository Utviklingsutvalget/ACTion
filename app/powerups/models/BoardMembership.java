package powerups.models;

import models.Club;
import models.User;

import javax.persistence.*;

@Entity
public class BoardMembership {

    @EmbeddedId
    private BoardMembershipKey key;
    @ManyToOne
    @JoinColumn(name = "club_id", insertable = false, updatable = false)
    private Club club;
    @ManyToOne
    @JoinColumn(name = "board_post_id", insertable = false, updatable = false)
    private BoardPost boardPost;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    private int weight;

    public BoardMembership(Club club, BoardPost boardPost, User user) {
        this.boardPost = boardPost;
        this.club = club;
        this.user = user;
        this.weight = boardPost.weight;
        this.key = new BoardMembershipKey(club, boardPost);
    }

    public BoardMembershipKey getKey() {
        return key;
    }

    public void setKey(final BoardMembershipKey key) {
        this.key = key;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(final Club club) {
        this.club = club;
    }

    public BoardPost getBoardPost() {
        return boardPost;
    }

    public void setBoardPost(final BoardPost boardPost) {
        this.boardPost = boardPost;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(final int weight) {
        this.weight = weight;
    }

    @Embeddable

    public class BoardMembershipKey {

        private Long clubId;
        private Long BoardPostId;

        public BoardMembershipKey(Club club, BoardPost boardPost) {

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

        public Long getClubId() {
            return clubId;
        }

        public void setClubId(final Long clubId) {
            this.clubId = clubId;
        }

        public Long getBoardPostId() {
            return BoardPostId;
        }

        public void setBoardPostId(final Long boardPostId) {
            BoardPostId = boardPostId;
        }
    }

}
