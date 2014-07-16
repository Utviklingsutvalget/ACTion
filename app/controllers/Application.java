package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;
import views.html.privacypolicy;

public class Application extends Controller {
    public static Result index() {
        return ok(index.render("Welcome"));
    }

    public static Result privacy() {
        return ok(privacypolicy.render());
    }
}
