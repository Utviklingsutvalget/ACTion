package models;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Event {

    @Id
    public String id;

    public String cover;
}
