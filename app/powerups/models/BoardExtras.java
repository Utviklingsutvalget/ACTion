package powerups.models;

import models.Club;
import models.Membership;
import models.User;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;

@Entity
public class BoardExtras extends Model {

    @ManyToOne
    public Board board;

    @Constraints.Required
    @OneToOne
    @PrimaryKeyJoinColumn
    public Membership member;

    @Constraints.Required
    public String title;

}
