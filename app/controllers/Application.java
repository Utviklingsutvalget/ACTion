package controllers;

import com.google.inject.Inject;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {

    @Inject
    private Feeds feedController;

    public Result index() {
        return feedController.index();
    }
}
