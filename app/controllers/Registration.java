package controllers;

import helpers.UserService;
import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.user.registration.form;
import views.html.user.registration.terms;

public class Registration extends Controller {

    public static Form<User> registrationForm = Form.form(User.class);

    public static Result submit() {

        Form<User> filledForm = registrationForm.bindFromRequest();

        if (!"true".equals(filledForm.field("accept").value()))
            filledForm.reject("accept", "Du må akseptere brukeravtalen");

        if (filledForm.hasErrors()) {
            return badRequest(form.render(filledForm));
        }

        User user = filledForm.get();
        UserService.save(user);

        OAuth2.createSessions(user.getId());

        return Users.profile();
    }

    public static Result autofill(User user) {

        Form<User> filledForm = registrationForm.fill(user);
        return ok(form.render(filledForm));

    }

    public static Result autoUpdate(User user) {

        UserService.update(user);
        return Users.profile();

    }

    public static Result terms() {
        return ok(terms.render());
    }
}
