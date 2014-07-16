package powerups.core.boardpowerup;

import models.User;

public class BoardMember {

    private final User member;

    private final String title;

    public BoardMember(User member, String title) {
        this.member = member;
        this.title = title;
    }

    public User getMember() {
        return member;
    }

    public String getTitle() {
        return title;
    }
}
