package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import helpers.UserService;
import models.User;
import play.Logger;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import utils.Authorize;
import views.html.user.profile;
import views.html.user.show;

public class Users extends Controller {
    /**
     * User proifle page
     *
     * If the user is not logged in, or if the sessions have expired
     * the user is redirected to the login page.
     *
     * @return Result
     */
    public Result profile() {

        try {
            User user = new Authorize.UserSession().getUser();
            boolean admin = user.isAdmin();
            return ok(profile.render(user, admin));
        } catch (Authorize.SessionException e) {
            return Results.redirect(routes.OAuth2.login());
        }
    }

    public Result show(final String id) {
        User user = UserService.findById(id);
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
    public Result logout() {
        OAuth2.destroySessions();
        return Application.index();
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result hasUserEmail() {
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
            User targetUser = UserService.findByEmail(email);
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
