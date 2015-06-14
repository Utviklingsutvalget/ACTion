package controllers;

import com.feth.play.module.pa.controllers.Authenticate;
import com.google.inject.Inject;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {

    @Inject
    private Feeds feedController;

    public Result authenticateDefault() {
        return authenticate("google");
    }

    public Result oAuthDenied(String provider) {
        return play.mvc.Results.TODO;
    }

    public Result index() {
        return feedController.index();
    }

    public Result logout() {
        return Authenticate.logout();
    }

    public Result authenticate(String provider) {
        return Authenticate.authenticate(provider);
    }
}
