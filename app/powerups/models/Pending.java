package powerups.models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.annotation.Transactional;
import models.Club;
import models.User;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import javax.persistence.*;

@Entity
public class Pending extends Model {

    public static Finder<PendingKey, Pending> find = new Finder<>(PendingKey.class, Pending.class);

    @EmbeddedId
    public PendingKey key;

    @Constraints.Required
    @Constraints.MaxLength(100)
    @Column(length=100)
    public String applicationMessage;

    @MapsId("clubId")
    @ManyToOne
    public Club club;

    @MapsId("userId")
    @ManyToOne
    public User user;

    public Pending(Club club, User user) {
        this.user = user;
        this.club = club;
        this.key = new PendingKey(user, club);
    }

    @Transactional
    public static void update(Pending pending){
        Ebean.save(pending);
    }

    @Embeddable
    public class PendingKey{

        public String userId;

        public Long clubId;

        public PendingKey(User user, Club club){
            this.userId = user.id;
            this.clubId = club.id;
        }

        @Override
        public int hashCode(){
            return super.hashCode();
        }

        @Override
        public boolean equals(Object other) {
            if(other == this) return true;

            if(other instanceof PendingKey){

                PendingKey p = (PendingKey)other;

                return p.clubId.equals(this.clubId) && p.userId.equals(this.userId);

            }else{
                return false;
            }
        }
    }

}
