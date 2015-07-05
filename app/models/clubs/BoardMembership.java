package models.clubs;

import models.User;
import models.composite.ClubUserKey;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class BoardMembership {

    @EmbeddedId
    private ClubUserKey id;
    @ManyToOne
    @JoinColumn(name = "club_id", insertable = false, updatable = false)
    private Club club;
    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
    private String title;
    private int weight;

    public BoardMembership() {
    }

    public BoardMembership(String email) {
        user = new User();
        user.setEmail(email);
    }

    public BoardMembership(Club club, String boardPost, User user, int weight) {
        this.title = boardPost;
        this.club = club;
        this.user = user;
        this.weight = weight;
        this.id = new ClubUserKey(club, user);
    }

    public ClubUserKey getId() {
        return id;
    }

    public void setId(final ClubUserKey id) {
        this.id = id;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(final Club club) {
        this.club = club;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(final int weight) {
        this.weight = weight;
    }
}