package powerups.core.boardpowerup;

import models.User;
import play.Logger;

public class BoardMember {

    private User member;

    //in order to set new titles
    private String title;

    public BoardMember(User member, String title) {
        Logger.warn("Setting up boardmember");
        this.member = member;
        this.title = title;
        Logger.warn("Set up boardmember");
    }

    public void setTitle(String title){
        this.title = title;
    }

    public User getMember() {
        return member;
    }

    public String getTitle() {
        return title;
    }
}
