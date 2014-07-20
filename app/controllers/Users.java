package controllers;

import models.User;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import utils.Authorize;
import views.html.index;
import views.html.user.profile;
import views.html.user.show;

import java.util.List;

public class Users extends Controller {
    public static Result index() {

        List<User> users = User.find.all();

        return ok(views.html.user.index.render(users));
    }

    public static Result delete(String id) {

        OAuth2.destroySessions();
        User.find.where().eq("id", id).findUnique().delete();
        return index();
    }

    /**
     * User proifle page
     *
     * ----------------------------------------------------------------------------
     * If the user is not logged in, or if the sessions have expired
     * the user is redirected to OAuth2.authenticate which functions as
     * the login page.
     *
     * A future feature for the OAuth2 could be, sending the users original preferred
     * url to the OAuth that represents the endpoint where the user was going before
     * he/she was redirected. That way when the user is logged in he/she would be
     * sent back to the page that they originally wanted.
     *----------------------------------------------------------------------------
     *
     * @return   Result
     */
    public static Result profile() {

        try {
            Authorize.UserSession session = new Authorize.UserSession();
            return ok(profile.render(session.getUser()));

        } catch(Authorize.SessionException e) {
            return Results.redirect(routes.OAuth2.authenticate(0));
        }
    }

    public static Result show(final String id) {
        User user = User.findById(id);
        User loggedInUser = null;
        try {
            loggedInUser = new Authorize.UserSession().getUser();
        } catch (Authorize.SessionException e) {
            return redirect(routes.OAuth2.authenticate(0));
        }
        if(user == null || user.equals(loggedInUser)) {
            return redirect(routes.Users.profile());
        } else {
            return ok(show.render(user));
        }
    }

    /**
     * Logs the user out.
     *
     * @return   Result
     */
    public static Result logout() {

        OAuth2.destroySessions();
        return ok(index.render("Logged Out"));
    }

}
