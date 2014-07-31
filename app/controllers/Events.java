package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import models.Event;
import models.Participation;
import models.User;
import org.joda.time.LocalDateTime;
import play.Logger;
import play.data.Form;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Authorize;
import utils.EventSorter;
import views.html.error;

import java.util.*;

import static play.data.Form.form;

public class Events extends Controller {

    public static int EVENT_DURATION = 6;

    public static Result index() {
        List<Event> events = Event.find.all();
        List<Event> allEvents = new ArrayList<>();
        events.sort(new EventSorter());
        List<Event> attendingEvents = new ArrayList<>();
        User user = null;
        try {
            user = new Authorize.UserSession().getUser();
            for (Event e : events) {
                if(!e.startTime.isBefore(LocalDateTime.now().plusHours(EVENT_DURATION))) {
                    allEvents.add(e);
                }
                Participation participation = new Participation(e, user);
                int i;
                if (e.participants.contains(participation)) {
                    i = e.participants.indexOf(participation);
                    participation = e.participants.get(i);
                    e.setUserHosting(participation.rvsp == Participation.Status.HOSTING);
                    if(participation.getRvsp()) {
                        attendingEvents.add(e);
                    }
                }
                e.setUserAttending(participation.getRvsp());
            }
        } catch (Authorize.SessionException ignored) {

        }
        final boolean loggedIn = user != null;
        return ok(views.html.event.index.render(allEvents, attendingEvents, loggedIn));
    }

    public static Result show(Long id) {

        Event event = Event.find.byId(id);
        User user = null;
        try {
            user = new Authorize.UserSession().getUser();
        } catch (Authorize.SessionException ignored) {}
        Participation participation = new Participation(event, user);
        if(event.participants.contains(participation)) {
            int i = event.participants.indexOf(participation);
            participation = event.participants.get(i);
            event.setUserAttending(event.participants.get(i).getRvsp());
        }
        if(user != null) {
            return ok(views.html.event.show.render(event, true));
        } else {
            return ok(views.html.event.show.render(event, false));
        }

    }

    public static Result create() {

        try {
            User user = new Authorize.UserSession().getUser();

            Form<Event> eventForm = form(Event.class);
            return ok(views.html.event.createForm.render(eventForm));

        } catch (Authorize.SessionException e) {
            return badRequest(error.render(e.getMessage()));
        }
    }

    public static Result save() {

        try {
            User user = new Authorize.UserSession().getUser();

            Form<Event> eventForm = form(Event.class).bindFromRequest();
            if (eventForm.hasErrors()) {
                return badRequest(views.html.event.createForm.render(eventForm));
            }

            eventForm.get().save();
            flash("success", "Event " + eventForm.get().name + " has been created.");
            return index();

        } catch (Authorize.SessionException e) {
            return badRequest(error.render(e.getMessage()));
        }


    }

    public static Result edit(Long id) {
        Form<Event> eventForm = form(Event.class).fill(
                Event.find.byId(id));

        return ok(views.html.event.editForm.render(id, eventForm));
    }

    public static Result update(Long id) {
        Form<Event> eventForm = form(Event.class).bindFromRequest();
        if (eventForm.hasErrors()) {
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

    @BodyParser.Of(BodyParser.Json.class)
    public static Result attend() {
        User user;
        try {
            user = new Authorize.UserSession().getUser();
        } catch (Authorize.SessionException e) {
            return unauthorized();
        }
        JsonNode json = request().body().asJson();
        Long eventId = json.findValue("event").asLong();
        boolean newRvsp = json.findValue("attend").asBoolean();
        Event event = Event.find.byId(eventId);
        if(event == null) {
            return internalServerError("No such event");
        }
        Participation participation = Participation.find.byId(new Participation(event, user).id);
        if(participation == null) {
            participation = new Participation(event, user);
            participation.setRvsp(newRvsp);
            Ebean.save(participation);
        } else {
            if(participation.getRvsp() != newRvsp) {
                participation.setRvsp(newRvsp);
                Ebean.update(participation);
            }
        }
        return ok();
    }

}
