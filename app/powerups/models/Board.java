package powerups.models;


import models.Club;
import models.Membership;
import models.User;
import org.apache.commons.lang3.ArrayUtils;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.*;

@Entity
public class Board extends Model {
    public static Finder<Long, Board> find = new Finder<>(Long.class, Board.class);

    public static final String[] MANDATORY = {
            "Leder", "Nestleder", "Økonomiansvarlig", "Eventansvarlig"
    };

    private enum TitleColumn {
        LEADER(LEADER_COL),
        VICE(VICE_COL),
        ECONOMY(ECON_COL),
        EVENT(EVENT_COL);
        private final String columnName;

        TitleColumn(String columnName) {

            this.columnName = columnName;
        }

        @Override
        public String toString() {
            return this.columnName;
        }

    }

    public static final String LEADER_COL = "leader";
    public static final String VICE_COL = "vice";
    public static final String ECON_COL = "economy";
    public static final String EVENT_COL = "event";

    public void setByName(final String name, final User user) {
        switch (name) {
            case LEADER_COL : leader = user;
                break;
            case VICE_COL : vice = user;
                break;
            case ECON_COL : economy = user;
                break;
            case EVENT_COL : event = user;
                break;
        }
    }

    @Id
    public Long clubID;

    @OneToOne
    public Club club;

    @OneToOne
    @PrimaryKeyJoinColumn
    @Column(name = LEADER_COL)
    public User leader;

    @OneToOne
    @Column(name = VICE_COL)
    @PrimaryKeyJoinColumn
    public User vice;

    @OneToOne
    @Column(name = ECON_COL)
    @PrimaryKeyJoinColumn
    public User economy;

    @OneToOne
    @Column(name = EVENT_COL)
    @PrimaryKeyJoinColumn
    public User event;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "board")
    public List<BoardExtras> boardExtra;

    @Embeddable
    public class BoardKey {

        public Long clubId;

        public boolean equals(Object other) {
            return other == this || other instanceof BoardKey && ((BoardKey) other).clubId.equals(this.clubId);
        }

        public int hashCode() {
            return super.hashCode();
        }
    }

    public List<String> getMandatoryPositions() {
        return Arrays.asList(MANDATORY);
    }

    public Map<String, String> getTitleColumns() {
        Map<String, String> returnMap = new HashMap<>();
        TitleColumn[] titleColumns = TitleColumn.values();

        int i = 0;
        for(String key : getMandatoryPositions()) {
            if(i < TitleColumn.values().length) {
                returnMap.put(key, titleColumns[i].name());
                i++;
            } else break;
        }
        return returnMap;
    }
}
