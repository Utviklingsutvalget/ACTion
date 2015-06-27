package models.clubs;

import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class BoardPost {

    @Id
    private Long id;

    @Constraints.Required
    private String title;
    @Constraints.Required
    private boolean mandatory;
    @Constraints.Required
    private int weight;

    public BoardPost(String title, boolean mandatory, int weight) {
        this.title = title;
        this.mandatory = mandatory;
        this.weight = weight;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(final boolean mandatory) {
        this.mandatory = mandatory;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(final int weight) {
        this.weight = weight;
    }
}
