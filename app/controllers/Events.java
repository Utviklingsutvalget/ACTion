package controllers;

import models.Event;
import play.data.*;
import static play.data.Form.*;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

public class Events extends Controller {

    public static Result index() {

        List<Event> events = Event.find.all();
        return ok(views.html.event.index.render(events));
    }

    public static Result show(Long id) {

        Event event = Event.find.byId(id);
        return ok(views.html.event.show.render(event));
    }

    public static Result create() {

        Form<Event> eventForm = form(Event.class);
        return ok(views.html.event.createForm.render(eventForm));
    }

    public static Result save() {
        Form<Event> eventForm = form(Event.class).bindFromRequest();
        if(eventForm.hasErrors()) {
            return badRequest(views.html.event.createForm.render(eventForm));
        }

        eventForm.get().save();
        flash("success", "Event " + eventForm.get().name + " has been created.");
        return index();
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
