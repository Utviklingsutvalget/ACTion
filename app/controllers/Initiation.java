package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class Initiation extends Controller {

    public static Result index() {
        return ok(views.html.initiation.index.render());
    }
}
