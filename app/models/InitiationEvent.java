package models;

import play.data.validation.Constraints;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class InitiationEvent {
    @Id
    private Long id;
    @Constraints.Required
    private LocalDateTime time;
    @Constraints.Required
    private String title;
    @Constraints.Required
    @Column (length = 300)
    private String description;
    @Constraints.Required
    private String location;
    @ManyToOne
    @Constraints.Required
    private InitiationSchedule initiationSchedule;

    public InitiationEvent(Long id, LocalDateTime time, String title, String description, String location) {
        this.id = id;
        this.time= time;
        this.title= title;
        this.description= description;
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
