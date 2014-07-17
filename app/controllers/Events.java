package controllers;

import models.Event;
import models.User;
import play.Logger;
import play.data.*;
import static play.data.Form.*;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Authorization;
import views.html.error;

import java.util.List;

public class Events extends Controller {

    public static final int EVENTS_PER_PAGE = 10;

    public static Result index() {
        return list(0, "name", "asc", "");
    }

    public static Result list(int page, String sortBy, String order, String filter) {

        return ok(views.html.event.list.render(
                Event.page(page, EVENTS_PER_PAGE, sortBy, order, filter),sortBy, order, filter));
    }

    public static Result show(Long id) {

        Event event = Event.find.byId(id);

        try {
            User user =  new Authorization.UserSession().getUser();
            return ok(views.html.event.show.render(event, user));

        } catch(Authorization.SessionException e) {return ok(views.html.event.show.render(event, null));}

    }

    public static Result create() {

        try {
            User user =  new Authorization.UserSession().getUser();

            Form<Event> eventForm = form(Event.class);
            return ok(views.html.event.createForm.render(eventForm));

        } catch(Authorization.SessionException e) {return badRequest(error.render(e.getMessage()));}
    }

    public static Result save() {

        try {
            User user =  new Authorization.UserSession().getUser();

            Form<Event> eventForm = form(Event.class).bindFromRequest();
            if(eventForm.hasErrors()) {
                return badRequest(views.html.event.createForm.render(eventForm));
            }

            eventForm.get().save();
            flash("success", "Event " + eventForm.get().name + " has been created.");
            return index();

        } catch(Authorization.SessionException e) {return badRequest(error.render(e.getMessage()));}


    }

    public static Result edit(Long id) {
        Form<Event> eventForm = form(Event.class).fill(
                Event.find.byId(id));

        return ok(views.html.event.editForm.render(id, eventForm));
    }

    public static Result update(Long id) {
        Form<Event> eventForm = form(Event.class).bindFromRequest();
        if(eventForm.hasErrors()) {
            return badRequest(views.html.event.editForm.render(id, eventForm));
        }
        eventForm.get().update(id);
        flash("success", "Event " + eventForm.get().name + " has been updated");
        return index();
    }

    public static Result delete(Long id) {

        Event event = Event.find.byId(id);
        Event.find.ref(id).delete();
        flash("success", "Event " + event.name + " has been deleted");

        return index();
    }
}
