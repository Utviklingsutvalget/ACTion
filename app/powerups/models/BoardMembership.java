package powerups.models;

import models.Club;
import models.User;
import powerups.models.composite.BoardMembershipKey;

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
        this.weight = boardPost.getWeight();
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

}
