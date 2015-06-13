package models.composite;

import javax.persistence.Embeddable;

@Embeddable
public class UserKey {

    private String userId;

    public UserKey(String userId) {

        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserKey userKey = (UserKey) o;

        return userId.equals(userKey.userId);

    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }
}
