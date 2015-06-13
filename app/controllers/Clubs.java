package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import services.UserService;
import models.*;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import powerups.Powerup;
import services.ActivationService;
import services.ClubService;
import utils.ActivationSorter;
import utils.Authorize;
import utils.MembershipLevel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Clubs extends Controller {

    @Inject
    private ClubService clubService;
    @Inject
    private ActivationService activationService;
    @Inject
    private UserService userService;

    public Result index() {
        List<Location> locations = Location.find.all();
        List<Club> byLocation = clubService.findByLocation(null);

        // Set up pseudo-location(null in database) to hold all global clubs
        Location global = new Location();
        global.name = "Felles";
        global.clubs.addAll(byLocation);
        locations.add(0, global);

        int cssId = 0;
        for (Location location : locations) {
            cssId++;
            location.cssId = cssId;
        }
        return ok(views.html.club.index.render(locations));
    }

    public Result show(Long id) {
        Club club = clubService.findById(id);
        if (club == null) {
            return redirect(routes.Clubs.index());
        }

        club.powerups = new ArrayList<>();
        // Sort the activations by weight:
        Collections.sort(club.activations, new ActivationSorter());

        for (Activation activation : club.activations) {
            Powerup powerup = activation.getPowerup();
            club.powerups.add(powerup);
        }
        return ok(views.html.club.show.render(club));
    }

    public Result update() {
        final Map<String, String[]> postValues = request().body().asFormUrlEncoded();

        final Long id = Long.valueOf(postValues.get("id")[0]);
        final String newName = postValues.get("name")[0];
        final Club club = clubService.findById(id);

        club.name = newName;
        //club.description = newDescription;

        Club.update(club);

        return redirect(routes.Clubs.index());
    }

    public Result create() {
        try {
            User user = new Authorize.UserSession().getUser();
            boolean authorized = user.isAdmin();
            if (!authorized) {
                return unauthorized();
            }
        } catch (Authorize.SessionException e) {
            e.printStackTrace();
        }

        Map<String, String[]> form = request().body().asFormUrlEncoded();
        for (String key : form.keySet()) {
            Logger.warn(key + ":" + form.get(key)[0]);
        }
        String name = form.get("name")[0];
        String shortName = form.get("shortName")[0];
        String email = form.get("leader")[0] + form.get("postfix")[0];
        Long locationId = Long.valueOf(form.get("location")[0]);

        Location location = Location.find.byId(locationId);

        User leaderUser = userService.findByEmail(email);

        if (leaderUser == null) {
            return badRequest("Det finnes ingen bruker tilknyttet den emailen, " +
                    "vennligst skriv inn en email knyttet til en bruker");
        }

        if (name.equals("") || shortName.equals("")) {
            return badRequest("Utvalget kan ikke ha et tomt Navn eller tom forkortelse");
        }

        Club club = new Club(name, shortName, location);
        clubService.save(club);

        Membership membership = new Membership(club, leaderUser, MembershipLevel.LEADER);
        club.members.add(membership);

        ArrayList<Activation> activations = new ArrayList<>();
        PowerupModel.find.all().stream().filter(model -> model.isMandatory).forEach(model -> {
            Activation activation = new Activation(club, model, model.defaultWeight);
            club.activations.add(activation);
            activations.add(activation);
        });

        membership.save();

        for (Activation activation : activations) {
            activationService.save(activation);
            activation.getPowerup().activate();
        }
        return redirect(routes.Clubs.show(club.id));
    }


    public Result updatePowerup(Long clubId, Long powerupId) {
        JsonNode json = request().body().asJson();
        Logger.warn("RECEIVED JSON");
        if (json == null || json.isNull()) {
            return badRequest("Expecting Json data");
        }
        final Club club = clubService.findById(clubId);
        Powerup powerup = null;
        for (Activation activation : club.activations) {
            if (activation.powerup.id.equals(powerupId)) {
                powerup = activation.getPowerup();
            }
        }
        if (powerup == null) {
            return badRequest("No such powerup for " + club.shortName);
        } else return powerup.update(json);
    }

    /**
     * WARNING: THIS RETURNS THE ADMIN FUNCTION
     */
    public Result getPowerupContent(Long clubId, Long powerupId) {
        final Club club = clubService.findById(clubId);

        Powerup powerup = null;

        if (club == null)
            return notFound(views.html.index.render("Utvalget du leter etter finnes ikke."));

        for (Activation activation : club.activations) {
            if (activation.powerup.id.equals(powerupId)) {
                powerup = activation.getPowerup();
            }
        }
        if (powerup == null) {
            return notFound(views.html.index.render("Poweruppen du leter etter finnes ikke for" + club.shortName));
        } else return ok(powerup.renderAdmin());
    }

}
