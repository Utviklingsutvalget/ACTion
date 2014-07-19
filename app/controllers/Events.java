package controllers;

import models.Participation;
import models.Event;
import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Authorize;
import utils.EventSorter;
import views.html.error;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static play.data.Form.form;

public class Events extends Controller {

    public static Result index() {
        List<Event> events = Event.find.all();
        List<Event> attendingEvents = new ArrayList<>();
        User user;
        try {
            user = new Authorize.UserSession().getUser();
            if(user != null) {
                for (Event e : events) {
                    if (e.participants.contains(new Participation(e, user))) {
                        attendingEvents.add(e);
                    }
                }
            }
        } catch (Authorize.SessionException ignored) {}
        events.sort(new EventSorter());

        return ok(views.html.event.index.render(events, attendingEvents));
    }

    public static Result show(Long id) {

        Event event = Event.find.byId(id);

        try {
            User user =  new Authorize.UserSession().getUser();
            return ok(views.html.event.show.render(event, user));

        } catch(Authorize.SessionException e) {return ok(views.html.event.show.render(event, null));}

    }

    public static Result create() {

        try {
            User user =  new Authorize.UserSession().getUser();

            Form<Event> eventForm = form(Event.class);
            return ok(views.html.event.createForm.render(eventForm));

        } catch(Authorize.SessionException e) {return badRequest(error.render(e.getMessage()));}
    }

    public static Result save() {

        try {
            User user =  new Authorize.UserSession().getUser();

            Form<Event> eventForm = form(Event.class).bindFromRequest();
            if(eventForm.hasErrors()) {
                return badRequest(views.html.event.createForm.render(eventForm));
            }

            eventForm.get().save();
            flash("success", "Event " + eventForm.get().name + " has been created.");
            return index();

        } catch(Authorize.SessionException e) {return badRequest(error.render(e.getMessage()));}


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

    public static Result participate() {

        Map<String, String[]> params = request().body().asFormUrlEncoded();

        Long eventId = Long.parseLong(params.get("event_id")[0]);
        String userId = params.get("user_id")[0];

        Event event = Event.find.byId(eventId);
        User user = User.findById(userId);

        Participation.save(new Participation(event, user));

        return show(event.id);
    }

    public static Result unparticipate() {

        Map<String, String[]> params = request().body().asFormUrlEncoded();

        Long uieId = Long.parseLong(params.get("uie_id")[0]);

        Long eventId = Participation.find.byId(uieId).event.id;
        Participation.find.byId(uieId).delete();

        return show(eventId);
    }
}
