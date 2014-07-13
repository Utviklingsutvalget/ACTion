package controllers;

import models.Club;
import models.Location;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;
import util.Action;
import util.Context;
import utils.Authorization;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Clubs extends Controller {
    public static Result index() {
        List<Location> locations = Location.find.all();

        // Set up pseudo-location(null in database) to hold all global clubs
        Location global = new Location();
        global.name = "Felles";
        global.clubs.addAll(Club.find.where().eq("location", null).findList().stream().collect(Collectors.toList()));
        locations.add(0, global);

        int cssId = 0;
        for(Location location : locations) {
            cssId++;
            location.cssId = cssId;
        }
        return ok(views.html.club.index.render(locations));
    }

    public static Result show(String id) {
        final Long clubId = Long.valueOf(id);

        final Club club = Club.find.byId(clubId);

        //Would this make sense?
        User user = Authorization.authorizeUserSession();
        Context context = new Context(user, club, Action.UPDATE);

        return ok(views.html.club.show.render(club, context));
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
