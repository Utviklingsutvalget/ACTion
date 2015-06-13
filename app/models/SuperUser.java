package models;

import models.composite.UserKey;

import javax.persistence.*;

@Entity
public class SuperUser {
    @EmbeddedId
    private UserKey key;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    public SuperUser(User user) {

        this.user = user;
        this.key = new UserKey(user.getId());
    }

    public UserKey getKey() {
        return key;
    }

    public void setKey(final UserKey key) {
        this.key = key;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

}
