package powerups.models;

import models.Club;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;

@Entity
@Table(name = "pl_ClubDescription")
public class ClubDescription extends Model {
    public static Finder<Long, ClubDescription> find = new Finder<>(Long.class, ClubDescription.class);

    @Id
    @OneToOne
    public Long clubId;

    @Constraints.Required
    public String description;

    @Constraints.Required
    public String listDescription;

}
