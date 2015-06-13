package models;

import com.avaje.ebean.Ebean;
import play.db.ebean.Transactional;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.List;

import static play.data.validation.Constraints.Required;

@Entity
public class Location {

    @Id
    private long id;
    @Required
    private String name;
    @OneToMany
    private List<Club> clubs;
    @Transient
    private int cssId;

    @Transactional
    public static void update(Location location) {
        Ebean.update(location);
    }

    public int getCssId() {
        return cssId;
    }

    public void setCssId(final int cssId) {
        this.cssId = cssId;
    }

    public List<Club> getClubs() {
        return clubs;
    }

    public void setClubs(final List<Club> clubs) {
        this.clubs = clubs;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }


}
