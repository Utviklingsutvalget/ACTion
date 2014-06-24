package powerups.models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name= "pl_ClubDescription")
public class ClubDescription extends Model {
    public static Finder<Long, ClubDescription> find = new Finder<>(Long.class, ClubDescription.class);

    @Id
    public Long clubId;

    @Constraints.Required
    public String description;

}
