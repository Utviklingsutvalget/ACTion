package controllers;

import com.google.inject.Inject;
import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.twirl.api.Html;
import services.UserService;
import views.html.user.registration.form;
import views.html.user.registration.terms;

public class Registration extends Controller {

    // TODO MAKE MORE SENSE
    public static final Html TERM_LINK = new Html("<a href=\"" + routes.Registration.terms() + "\" target=\"_blank\">brukervilkårene</a>");
    public static final String TERM_STRING = "Du samtykker at du har lest og godtatt " + TERM_LINK + ".";
    public static Form<User> registrationForm = Form.form(User.class);
    @Inject
    private OAuth2 oAuth2Controller;
    @Inject
    private Users userController;
    @Inject
    private UserService userService;

    public Result submit() {

        Form<User> filledForm = registrationForm.bindFromRequest();

        if (!"true".equals(filledForm.field("accept").value()))
            filledForm.reject("accept", "Du må akseptere brukervilkårene");

        if (filledForm.hasErrors()) {
            return badRequest(form.render(filledForm, TERM_STRING));
        }

        User user = filledForm.get();
        userService.save(user);

        oAuth2Controller.createSessions(user.getId());

        return userController.profile();
    }

    public Result autofill(User user) {
        Form<User> filledForm = registrationForm.fill(user);
        return ok(form.render(filledForm, TERM_STRING));

    }

    public Result autoUpdate(User user) {
        userService.update(user);
        return userController.profile();

    }

    public Result terms() {
        return ok(terms.render());
    }
}
