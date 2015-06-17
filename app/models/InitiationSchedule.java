package models;

import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.Year;
import java.util.List;

@Entity
public class InitiationSchedule {
    @Id
    private Long id;
    @Constraints.Required
    @ManyToOne
    private Location campus;
    @Constraints.Required
    @OneToMany
    private List<InitiationEvent> events;
    @Constraints.Required
    private Year year;

    public InitiationSchedule(Long id, Location campus, List<InitiationEvent> events, Year year) {
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

    public Location getCampus() {
        return campus;
    }

    public void setCampus(Location campus) {
        this.campus = campus;
    }

    public List<InitiationEvent> getEvents() {
        return events;
    }

    public void setEvents(List<InitiationEvent> events) {
        this.events = events;
    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }
}
