package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.User;
import play.Logger;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import utils.Authorize;
import views.html.index;
import views.html.user.profile;
import views.html.user.show;

public class Users extends Controller {
    /**
     * User proifle page
     * <p>
     * ----------------------------------------------------------------------------
     * If the user is not logged in, or if the sessions have expired
     * the user is redirected to OAuth2.authenticate which functions as
     * the login page.
     * <p>
     * A future feature for the OAuth2 could be, sending the users original preferred
     * url to the OAuth that represents the endpoint where the user was going before
     * he/she was redirected. That way when the user is logged in he/she would be
     * sent back to the page that they originally wanted.
     * ----------------------------------------------------------------------------
     *
     * @return Result
     */
    public static Result profile() {

        try {
            User user = new Authorize.UserSession().getUser();
            boolean admin = user.isAdmin();
            return ok(profile.render(user, admin));
        } catch (Authorize.SessionException e) {
            return Results.redirect(controllers.routes.OAuth2.authenticate(0));
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
        if (user == null || user.equals(loggedInUser)) {
            return redirect(routes.Users.profile());
        } else {
            return ok(show.render(user));
        }
    }

    /**
     * Logs the user out.
     *
     * @return Result
     */
    public static Result logout() {

        OAuth2.destroySessions();
        return ok(index.render("Logged Out"));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result hasUserEmail() {
        Logger.warn("RECEIVED REQUEST");
        User user;
        try {
            user = new Authorize.UserSession().getUser();
            boolean authorized = user.isAdmin();
            Logger.warn("Admin: " + authorized);
            JsonNode json = request().body().asJson();
            String email = json.findValue("email").asText();
            Logger.warn(email);
            if (!authorized) {
                return forbidden();
            }
            User targetUser = User.findByEmail(email);
            if (targetUser != null) {
                return ok();
            }
        } catch (Authorize.SessionException e) {
            return unauthorized();
        }
        Logger.warn("Did not find user");
        return notFound();
    }

}
