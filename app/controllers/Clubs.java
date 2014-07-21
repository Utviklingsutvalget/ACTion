package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Activation;
import models.Club;
import models.Location;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import powerups.Powerup;
import utils.ActivationSorter;

import java.util.ArrayList;
import java.util.Collections;
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

    public static Result show(Long id) {
        Club club = Club.find.byId(id);

        club.powerups = new ArrayList<>();
        // Sort the activations by weight:
        Collections.sort(club.activations, new ActivationSorter());

        for(Activation activation : club.activations) {
            Powerup powerup = activation.getPowerup();
            club.powerups.add(powerup);
        }

        return ok(views.html.club.show.render(club));
    }

    public static Result update() {
        final Map<String, String[]> postValues = request().body().asFormUrlEncoded();

        final Long id = Long.valueOf(postValues.get("id")[0]);
        final String newName = postValues.get("name")[0];
        final Club club = Club.find.byId(id);

        club.name = newName;
        //club.description = newDescription;

        Club.update(club);

        return redirect(routes.Clubs.index());
    }

    public static Result create() {
        //Form<Club> clubForm = Form.form(Club.class);
        //Club club = clubForm.bindFromRequest().get();
        return null;
    }


    public static Result updatePowerup(Long clubId, Long powerupId) {
        JsonNode json = request().body().asJson();

        if(json == null || json.isNull()) {
            return badRequest("Expecting Json data");
        }
        final Club club = Club.find.byId(clubId);
        Powerup powerup = null;
        for(Activation activation : club.activations) {
            if(activation.powerup.id.equals(powerupId)) {
                powerup = activation.getPowerup();
            }
        }
        if(powerup == null) {
            return badRequest("No such powerup for " + club.shortName);
        } else return powerup.update(json);
    }

}
