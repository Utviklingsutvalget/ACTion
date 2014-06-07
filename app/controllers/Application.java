package controllers;

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
                new User("Eivind", 12),
                new User("Martin", 21)
        ));
        return ok(index.render(users));
    }
}
