package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;
import views.html.debug;

public class Application extends Controller {
    public static Result index() {
        return ok(index.render());
    }

    public static Result debug() {
        return ok(debug.render("Message: "));
    }
}
