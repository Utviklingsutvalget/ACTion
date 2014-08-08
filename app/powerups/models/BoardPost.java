package powerups.models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class BoardPost extends Model {

    public static Finder<Long, BoardPost> find = new Finder<>(Long.class, BoardPost.class);

    @Id
    public Long id;

    @Constraints.Required
    public String title;

    @Constraints.Required
    public boolean isMandatory;

    @Constraints.Required
    public int weight;

    public BoardPost(String title, boolean isMandatory, int weight) {
        this.title = title;
        this.isMandatory = isMandatory;
        this.weight = weight;
    }
}
