package utils;

public enum MembershipLevel {
    SUBSCRIBE,
    MEMBER,
    BOARD,
    VICE,
    LEADER,
    COUNCIL;

    public int getLevel() {
        switch(this) {
            case SUBSCRIBE: return 0;
            case MEMBER: return 1;
            case BOARD: return 2;
            case VICE: return 3;
            case LEADER: return 4;
            case COUNCIL: return 5;
            default: return 0;
        }
    }
    public String getTitle() {
        switch(this) {
            case MEMBER: return "Medlem";
            case BOARD: return "Styremedlem";
            case VICE: return "Nestleder";
            case LEADER: return "Leder";
            case COUNCIL: return "Studentrådsleder";
            default: return "Ikke medlem";
        }
    }
}
