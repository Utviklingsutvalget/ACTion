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
import play.twirl.api.Content;
import services.EventService;
import services.ParticipationService;
import services.UserService;
import utils.EventSorter;

import java.util.ArrayList;
import java.util.List;

import static play.data.Form.form;

public class Events extends Controller {

    public int EVENT_DURATION = 6;

    @Inject
    private EventService eventService;
    @Inject
    private ParticipationService participationService;
    @Inject
    private UserService userService;

    public Result index() {
        List<Event> events = eventService.findAll();
        List<Event> allEvents = new ArrayList<>();
        events.sort(new EventSorter());
        List<Event> attendingEvents = new ArrayList<>();
        User user = userService.getCurrentUser(session());
        for (Event e : events) {
            if (!e.getStartTime().isBefore(LocalDateTime.now().plusHours(EVENT_DURATION))) {
                allEvents.add(e);
            }
            Participation participation = new Participation(e, user);
            int i;
            List<Participation> participants = e.getParticipants();
            if (participants.contains(participation)) {
                i = participants.indexOf(participation);
                participation = participants.get(i);
                e.setUserHosting(participation.getRvspObject() == Participation.Status.HOSTING);
                if (participation.getRvsp()) {
                    attendingEvents.add(e);
                }
            }
            e.setUserAttending(participation.getRvsp());
        }

        final boolean loggedIn = user != null;
        return ok((Content) views.html.event.index.render(allEvents, attendingEvents, loggedIn));
    }

    public Result show(Long id) {
        Event event = eventService.findById(id);
        User user = userService.getCurrentUser(session());
        Participation participation = new Participation(event, user);
        if (event.getParticipants().contains(participation)) {
            int i = event.getParticipants().indexOf(participation);
            participation = event.getParticipants().get(i);
            event.setUserHosting(participation.getRvspObject() == Participation.Status.HOSTING);
            event.setUserAttending(event.getParticipants().get(i).getRvsp());
        }
        if (user != null) {
            return ok((Content) views.html.event.show.render(event, true));
        } else {
            return ok((Content) views.html.event.show.render(event, false));
        }

    }

    public Result create() {
        User user = userService.getCurrentUser(session());

        Form<Event> eventForm = form(Event.class);
        return ok(views.html.event.createForm.render(eventForm));
    }

    public Result save() {
        User user = userService.getCurrentUser(session());

        Form<Event> eventForm = form(Event.class).bindFromRequest();
        if (eventForm.hasErrors()) {
            return badRequest(views.html.event.createForm.render(eventForm));
        }

        Event event = eventForm.get();
        eventService.save(event);
        flash("success", "Event " + eventForm.get().getName() + " has been created.");
        return index();

    }

    public Result edit(Long id) {
        Event event = eventService.findById(id);
        Form<Event> eventForm = form(Event.class).fill(
                event);

        return ok((Content) views.html.event.editForm.render(id, eventForm));
    }

    public Result update(Long id) {
        Form<Event> eventForm = form(Event.class).bindFromRequest();
        if (eventForm.hasErrors()) {
            return badRequest(views.html.event.editForm.render(id, eventForm));
        }
        Event event = eventForm.get();
        eventService.update(event);
        flash("success", "Event " + eventForm.get().getName() + " has been updated");
        return index();
    }

    public Result delete(Long id) {
        Event event = eventService.findById(id);

        eventService.delete(event);
        flash("success", "Event " + event.getName() + " has been deleted");

        return index();
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result attend() {
        User user = userService.getCurrentUser(session());
        if(user == null) {
            return unauthorized();
        }
        JsonNode json = request().body().asJson();
        Long eventId = json.findValue("event").asLong();
        boolean newRvsp = json.findValue("attend").asBoolean();
        Event event = eventService.findById(eventId);
        if (event == null) {
            return internalServerError("No such event");
        }
        Participation participation = participationService.findById(new Participation(event, user).getId());
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
