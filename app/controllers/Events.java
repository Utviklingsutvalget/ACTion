package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import models.Event;
import models.Participation;
import models.User;
import org.joda.time.LocalDateTime;
import play.data.Form;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.EventService;
import utils.Authorize;
import utils.EventSorter;
import views.html.error;

import java.util.ArrayList;
import java.util.List;

import static play.data.Form.form;

public class Events extends Controller {

    public int EVENT_DURATION = 6;

    @Inject
    private EventService eventService;

    public Result index() {
        List<Event> events = eventService.findAll();
        List<Event> allEvents = new ArrayList<>();
        events.sort(new EventSorter());
        List<Event> attendingEvents = new ArrayList<>();
        User user = null;
        try {
            user = new Authorize.UserSession().getUser();
            for (Event e : events) {
                if (!e.startTime.isBefore(LocalDateTime.now().plusHours(EVENT_DURATION))) {
                    allEvents.add(e);
                }
                Participation participation = new Participation(e, user);
                int i;
                if (e.participants.contains(participation)) {
                    i = e.participants.indexOf(participation);
                    participation = e.participants.get(i);
                    e.setUserHosting(participation.rvsp == Participation.Status.HOSTING);
                    if (participation.getRvsp()) {
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

    public Result show(Long id) {
        Event event = eventService.findById(id);
        User user = null;
        try {
            user = new Authorize.UserSession().getUser();
        } catch (Authorize.SessionException ignored) {
        }
        Participation participation = new Participation(event, user);
        if (event.participants.contains(participation)) {
            int i = event.participants.indexOf(participation);
            participation = event.participants.get(i);
            event.setUserHosting(participation.rvsp == Participation.Status.HOSTING);
            event.setUserAttending(event.participants.get(i).getRvsp());
        }
        if (user != null) {
            return ok(views.html.event.show.render(event, true));
        } else {
            return ok(views.html.event.show.render(event, false));
        }

    }

    public Result create() {

        try {
            User user = new Authorize.UserSession().getUser();

            Form<Event> eventForm = form(Event.class);
            return ok(views.html.event.createForm.render(eventForm));

        } catch (Authorize.SessionException e) {
            return badRequest(error.render(e.getMessage()));
        }
    }

    public Result save() {

        try {
            User user = new Authorize.UserSession().getUser();

            Form<Event> eventForm = form(Event.class).bindFromRequest();
            if (eventForm.hasErrors()) {
                return badRequest(views.html.event.createForm.render(eventForm));
            }

            Event event = eventForm.get();
            eventService.save(event);
            flash("success", "Event " + eventForm.get().name + " has been created.");
            return index();

        } catch (Authorize.SessionException e) {
            return badRequest(error.render(e.getMessage()));
        }


    }

    public Result edit(Long id) {
        Event event = eventService.findById(id);
        Form<Event> eventForm = form(Event.class).fill(
                event);

        return ok(views.html.event.editForm.render(id, eventForm));
    }

    public Result update(Long id) {
        Form<Event> eventForm = form(Event.class).bindFromRequest();
        if (eventForm.hasErrors()) {
            return badRequest(views.html.event.editForm.render(id, eventForm));
        }
        Event event = eventForm.get();
        eventService.update(event);
        flash("success", "Event " + eventForm.get().name + " has been updated");
        return index();
    }

    public Result delete(Long id) {
        Event event = eventService.findById(id);

        eventService.delete(event);
        flash("success", "Event " + event.name + " has been deleted");

        return index();
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result attend() {
        User user;
        try {
            user = new Authorize.UserSession().getUser();
        } catch (Authorize.SessionException e) {
            return unauthorized();
        }
        JsonNode json = request().body().asJson();
        Long eventId = json.findValue("event").asLong();
        boolean newRvsp = json.findValue("attend").asBoolean();
        Event event = eventService.findById(eventId);
        if (event == null) {
            return internalServerError("No such event");
        }
        Participation participation = Participation.find.byId(new Participation(event, user).id);
        if (participation == null) {
            participation = new Participation(event, user);
            participation.setRvsp(newRvsp);
            Ebean.save(participation);
        } else {
            if (participation.getRvsp() != newRvsp) {
                participation.setRvsp(newRvsp);
                Ebean.update(participation);
            }
        }
        return ok();
    }

}
