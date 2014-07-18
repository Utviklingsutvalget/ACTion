package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Page;
import play.Logger;
import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import play.db.ebean.Transactional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Event extends Model {

    public static Finder<Long, Event> find = new Finder<>(Long.class, Event.class);

    public static enum Privacy {OPEN, MEDLEMMER}

    @Id
    public Long id;

    @Constraints.Required
    public Privacy privacy;

    @Constraints.Required
    public String name;

    @Constraints.Required
    public String description;

    @Constraints.Required
    //@Formats.DateTime(pattern="dd-MM-yyyy")
    public Date startTime;

    @Constraints.Required
    //@Formats.DateTime(pattern="dd-MM-yyyy")
    public Date endTime;

    @Constraints.Required
    public String location;

    public String coverUrl;

    @ManyToOne
    public Club club;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "event")
    public List<UsersInEvent> participants = new ArrayList<>();

    public static Page<Event> page(int page, int pageSize, String sortBy, String order, String filter) {

        return find.where()
                .ilike("name", "%" + filter + "%")
                .orderBy(sortBy + " " + order)
                .fetch("club")
                .findPagingList(pageSize)
                .setFetchAhead(false)
                .getPage(page);
    }

    public static List<String> getPrivacyAsList(){
        ArrayList<String> list = new ArrayList<>();

        for(Privacy privacy : Privacy.values()) { list.add(privacy.name());}
        return list;
    }
}
