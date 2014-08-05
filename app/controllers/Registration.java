package controllers;

import models.User;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.user.registration.form;
import views.html.user.registration.terms;
import views.html.index;

public class Registration extends Controller {

    public static Form<User> registrationForm = Form.form(User.class);

    public static Result submit() {

        Form<User> filledForm = registrationForm.bindFromRequest();

        if(!"true".equals(filledForm.field("accept").value()))
            filledForm.reject("accept", "Du må akseptere brukeravtalen");

        if(filledForm.hasErrors()) {
            return badRequest(form.render(filledForm));
        }

        User user = filledForm.get();
        User.save(user);

        OAuth2.createSessions(user.id);

        return Users.profile();
    }

    public static Result autofill(User user) {

        Form<User> filledForm = registrationForm.fill(user);
        return ok(form.render(filledForm));

    }

    public static Result autoUpdate(User user) {

        User.update(user);
        return Users.profile();

    }

    public static Result terms() {
        return ok(terms.render());
    }
}
