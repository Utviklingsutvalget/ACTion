package controllers;

import models.Club;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;
import java.util.Map;

public class Clubs extends Controller {
    public static Result index() {
        final List<Club> clubs = Club.find.all();
        return ok(views.html.club.index.render(clubs));
    }

    public static Result show(String id) {
        final Long clubId = Long.valueOf(id);

        final Club club = Club.find.byId(clubId);

        return ok(views.html.club.show.render(club));
    }

    public static Result update() {
        final Map<String, String[]> postValues = request().body().asFormUrlEncoded();

        final Long id = Long.valueOf(postValues.get("id")[0]);
        final String newName = postValues.get("name")[0];
        final String newDescription = postValues.get("description")[0];

        final Club club = Club.find.byId(id);

        club.name = newName;
        club.description = newDescription;

        Club.update(club);

        return redirect(routes.Clubs.index());
    }
}
