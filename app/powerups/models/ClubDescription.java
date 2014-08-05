package powerups.models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "pl_ClubDescription")
public class ClubDescription extends Model {
    public static Finder<Long, ClubDescription> find = new Finder<>(Long.class, ClubDescription.class);

    @Id
    @OneToOne
    public Long clubId;

    @Constraints.Required
    @Constraints.MaxLength(10000)
    public String description;

    @Constraints.Required
    @Constraints.MaxLength(300)
    public String listDescription;

}
