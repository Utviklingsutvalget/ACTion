package models.clubs;

import models.User;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.List;

@Entity
public class ClubInfo {

    @EmbeddedId
    private ClubKey id;

    @JoinColumn(name = "club_id")
    @OneToOne(mappedBy = "info")
    private Club club;
    @OneToMany
    private User owner;
    @Constraints.Required
    @Constraints.MaxLength(10000)
    private String description;
    @Constraints.Required
    @Constraints.MaxLength(300)
    private String listDescription;
    @OneToMany(cascade = CascadeType.ALL)
    private List<BoardMembership> boardMemberships;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Pending> pendingMemberships;

    public User getOwner() {
        return owner;
    }

    public void setOwner(final User owner) {
        this.owner = owner;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(final Club club) {
        this.club = club;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getListDescription() {
        return listDescription != null ? listDescription : "";
    }

    public void setListDescription(final String listDescription) {
        this.listDescription = listDescription;
    }

    public List<BoardMembership> getBoardMemberships() {
        return boardMemberships;
    }

    public void setBoardMemberships(final List<BoardMembership> boardMemberships) {
        this.boardMemberships = boardMemberships;
    }

    public List<Pending> getPendingMemberships() {
        return pendingMemberships;
    }

    public void setPendingMemberships(final List<Pending> pendingMemberships) {
        this.pendingMemberships = pendingMemberships;
    }

    public ClubKey getId() {
        return id;
    }

    public void setId(final ClubKey id) {
        this.id = id;
    }
}
