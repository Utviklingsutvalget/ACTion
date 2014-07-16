package powerups.models;

import models.Club;
import models.User;
import play.db.ebean.Model;

import javax.persistence.*;
import javax.persistence.Id;

@Entity
public class Pending extends Model {

    /****Solution Two******/

    @Id
    public Long pendingId;


    /****Solution One******/

    /*
    @EmbeddedId
    public PendingKey key;

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
    }*/

}
