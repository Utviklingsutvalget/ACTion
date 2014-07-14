package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import utils.FileUtility;

import java.util.Map;

public class FacebookApp extends Controller {

    /**Secrets*/
    private static final Map<String, String> CONF = FileUtility.getMap("secrets/googleoauth", "=");

    public static Result authenticate() {

        //Redirect to google
        return redirect("https://graph.facebook.com/oauth/authorize?" +
                "client_id=" + CONF.get("app_id") +
                "&redirect_uri=http://localhost:9000/facebook/callbackhandler" +
                "&scope=rsvp_event");

    }

    public static Result exchange() {

        return ok();
    }
}
