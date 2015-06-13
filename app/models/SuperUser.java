package models;

import javax.persistence.*;

@Entity
public class SuperUser {
    @EmbeddedId
    private SuKey key;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    public SuperUser(User user) {

        this.user = user;
        this.key = new SuKey(user.getId());
    }

    public SuKey getKey() {
        return key;
    }

    public void setKey(final SuKey key) {
        this.key = key;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    @Embeddable
    public class SuKey {

        private String userId;

        public SuKey(String userId) {

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

            SuKey suKey = (SuKey) o;

            return userId.equals(suKey.userId);

        }

        @Override
        public int hashCode() {
            return userId.hashCode();
        }
    }
}
