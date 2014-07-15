package controllers;

import models.Event;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

public class Events extends Controller {

    public static Result index() {

        List<Event> events = Event.find.all();
        return ok(views.html.event.index.render(events));
    }

    public static Result create() {

        Form<Event> eventForm = form(Event.class);
        return ok(createForm.render(computerForm));
    }
}
