package models;

import play.data.validation.Constraints;

import javax.persistence.Id;
import java.time.Year;
import java.util.List;

public class FadderPlan {
    @Id
    private Long id;
    @Constraints.Required
    private String campus;
    @Constraints.Required
    private List<FadderEvent> events;
    @Constraints.Required
    private Year year;

    public FadderPlan(Long id, String campus, List<FadderEvent> events, Year year) {
        this.campus = campus;
        this.events = events;
        this.year = year;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public List<FadderEvent> getEvents() {
        return events;
    }

    public void setEvents(List<FadderEvent> events) {
        this.events = events;
    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }
}
