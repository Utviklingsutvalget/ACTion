package controllers;

import models.User;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Application extends Controller {
    public static Result index() {
        List<User> users = new ArrayList<>();

        users.addAll(Arrays.asList(
                new User("Eivind", User.Gender.FEMALE),
                new User("Martin", User.Gender.MALE)
        ));

        User user = new User("Rebecca", User.Gender.MALE);

        return ok(index.render(users));
    }
}
