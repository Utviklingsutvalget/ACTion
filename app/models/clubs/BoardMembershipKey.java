package models.clubs;

import javax.persistence.Embeddable;

@Embeddable
public class BoardMembershipKey {

    private Long clubId;
    private Long BoardPostId;

    public BoardMembershipKey(Club club, BoardPost boardPost) {

        this.clubId = club.getId();
        this.BoardPostId = boardPost.getId();
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
