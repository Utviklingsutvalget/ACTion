package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.event.index;

public class Application extends Controller {

    public static Result index() {
        return Events.index();
    }
}
