package controllers;

import models.User;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

public class Users extends Controller {
    public static Result index() {
        List<User> users = User.find.all();

        return ok(views.html.user.index.render(users));
    }
}
