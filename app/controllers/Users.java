package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import models.User;
import play.Logger;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.UserService;
import utils.Authorize;
import views.html.user.profile;
import views.html.user.show;

public class Users extends Controller {

    @Inject
    private OAuth2 oAuth2Controller;

    @Inject
    private Application applicationController;
    @Inject
    private UserService userService;

    /**
     * User proifle page
     *
     * If the user is not logged in, or if the sessions have expired
     * the user is redirected to the login page.
     *
     * @return Result
     */
    public Result profile() {
        User user = userService.getCurrentUser(session());
        if(user == null) {
            return redirect(routes.Application.authenticateDefault());
        }
        boolean admin = userService.isUserAdmin(user);
        return ok(profile.render(user, admin));
    }

    public Result show(final String id) {
        User user = userService.findById(id);
        User loggedInUser;
        try {
            loggedInUser = new Authorize.UserSession().getUser();
        } catch (Authorize.SessionException e) {
            return redirect(routes.Application.authenticateDefault());
        }
        if (user == null || user.equals(loggedInUser)) {
            return redirect(routes.Users.profile());
        } else {
            return ok(show.render(user));
        }
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result hasUserEmail() {
        Logger.warn("RECEIVED REQUEST");
        User user;
        user = userService.getCurrentUser(session());
        if(user == null) {
            return unauthorized();
        }
        boolean authorized = userService.isUserAdmin(user);
        Logger.warn("Admin: " + authorized);
        JsonNode json = request().body().asJson();
        String email = json.findValue("email").asText();
        Logger.warn(email);
        if (!authorized) {
            return forbidden();
        }
        User targetUser = userService.findByEmail(email);
        if (targetUser != null) {
            return ok();
        }
        Logger.warn("Did not find user");
        return notFound();
    }

}
