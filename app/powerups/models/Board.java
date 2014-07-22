package powerups.models;


import models.Club;
import models.User;
import play.Logger;
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

    public Board(User leader, User vice, User economy, User event){
        this.leader = leader;
        this.vice = vice;
        this.economy = economy;
        this.event = event;

        this.key = new BoardKey(this.club.id);
    }

    public Board(Club club){
        this.club = club;

        this.key = new BoardKey(this.club.id);
    }

    public static final String LEADER_COL = "leader";
    public static final String VICE_COL = "vice";
    public static final String ECON_COL = "economy";
    public static final String EVENT_COL = "event";

    public void setByName(final String name, final User user) {

        //switch on string case sensitive
        switch (name.toLowerCase()) {
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

    @EmbeddedId
    public BoardKey key;

    @OneToOne
    @JoinColumn(name = "club_id", insertable = false, updatable = false)
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
