package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class UsersInEvent extends Model {

    @Id
    public Long id;

    @ManyToOne
    public User user;

    @ManyToOne
    public Event event;
}