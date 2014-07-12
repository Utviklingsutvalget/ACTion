package controllers;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import play.mvc.Controller;
import play.mvc.Result;
import com.restfb.types.Event;

public class Events extends Controller {

    public static FacebookClient = new DefaultFacebookClient

    public Result index() {
        return ok();
    }

    public Result show(Long id) {
        return ok();
    }

    public Result add(Event e) {
        return ok();
    }

    public Result update(Event e) {
        return ok();
    }
}
