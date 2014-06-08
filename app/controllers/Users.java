package controllers;

import models.User;
import play.mvc.Result;
import views.html.user.index;

import java.util.List;

import static play.mvc.Results.ok;

public class Users {
    public static Result index() {
        List<User> users = User.find.all();

        return ok(views.html.user.index.render(users));
    }
}
