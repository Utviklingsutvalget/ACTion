package models.composite;


import javax.persistence.Embeddable;

@Embeddable
public class ParticipationKey {

    private Long eventId;
    private String userId;

    public ParticipationKey(final Long eventId, final String userId) {
        this.eventId = eventId;
        this.userId = userId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ParticipationKey that = (ParticipationKey) o;

        return eventId.equals(that.eventId) && userId.equals(that.userId);

    }

    @Override
    public int hashCode() {
        int result = eventId.hashCode();
        result = 31 * result + userId.hashCode();
        return result;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(final Long eventId) {
        this.eventId = eventId;
    }
}
