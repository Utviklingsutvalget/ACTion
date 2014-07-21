package models;

import play.db.ebean.Model;

import javax.persistence.*;

@Entity
public class SuperUser extends Model {
    public static Finder<SuKey, SuperUser> find = new Finder<>(SuKey.class, SuperUser.class);

    @EmbeddedId
    public SuKey key;

    @OneToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    public User user;

    public SuperUser(User user) {

        this.user = user;
        this.key = new SuKey(user.id);
    }

    @Embeddable
    public class SuKey {

        public String userId;

        public SuKey(String userId) {

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
