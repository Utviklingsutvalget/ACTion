package powerups.models;


import com.avaje.ebean.common.BeanList;
import models.Club;
import models.User;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.*;

@Entity
public class Board extends Model {
    public static final String[] MANDATORY = {
            "Leder", "Nestleder", "Økonomiansvarlig", "Eventansvarlig"
    };
    public static final String LEADER_COL = "leader";
    public static final String VICE_COL = "vice";
    public static final String ECON_COL = "economy";
    public static final String EVENT_COL = "event";
    @EmbeddedId
    public BoardKey key;
    @OneToOne
    @JoinColumn(name = "club_id", insertable = false, updatable = false)
    public Club club;
    @OneToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    public User leader;
    @OneToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    public User vice;
    @OneToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    public User economy;
    @OneToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    public User event;
    /*
    @OneToMany(mappedBy = "board")
    public List<BoardExtras> boardExtra = new BeanList<>();
*/
    public Board(Club club) {
        this.club = club;

        this.key = new BoardKey(this.club.id);
    }

    public void setByName(final String name, final User user) {

        //switch on string case sensitive
        switch (name.toLowerCase()) {
            case LEADER_COL:
                leader = user;
                break;
            case VICE_COL:
                vice = user;
                break;
            case ECON_COL:
                economy = user;
                break;
            case EVENT_COL:
                event = user;
                break;
        }
    }

    public List<String> getMandatoryPositions() {
        return Arrays.asList(MANDATORY);
    }

    public Map<String, String> getTitleColumns() {
        Map<String, String> returnMap = new HashMap<>();
        TitleColumn[] titleColumns = TitleColumn.values();

        int i = 0;
        for (String key : getMandatoryPositions()) {
            if (i < TitleColumn.values().length) {
                returnMap.put(key, titleColumns[i].name());
                i++;
            } else break;
        }
        return returnMap;
    }

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

    @Embeddable
    public class BoardKey {

        public Long clubId;

        public BoardKey(Long clubId) {
            this.clubId = clubId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            BoardKey boardKey = (BoardKey) o;

            return clubId.equals(boardKey.clubId);

        }

        @Override
        public int hashCode() {
            return clubId.hashCode();
        }
    }
}
