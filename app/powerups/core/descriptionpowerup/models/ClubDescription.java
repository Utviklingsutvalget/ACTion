package powerups.core.descriptionpowerup.models;

import models.Club;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name= "pl_ClubDescription")
public class ClubDescription extends Model {

    @Id
    public Club club;

    @Constraints.Required
    public String description;

}
