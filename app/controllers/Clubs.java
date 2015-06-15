package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import models.*;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.twirl.api.Content;
import powerups.Powerup;
import services.*;
import utils.ActivationSorter;
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
    @Inject
    private LocationService locationService;
    @Inject
    private PowerupService powerupService;
    @Inject
    private MembershipService membershipService;

    public Result index() {
        List<Location> locations = locationService.findAll();
        List<Club> byLocation = clubService.findByLocation(null);

        // Set up pseudo-location(null in database) to hold all global clubs
        Location global = new Location();
        global.setName("Felles");
        global.getClubs().addAll(byLocation);
        locations.add(0, global);

        int cssId = 0;
        for (Location location : locations) {
            cssId++;
            location.setCssId(cssId);
        }
        return ok((Content) views.html.club.index.render(locations));
    }

    public Result show(Long id) {
        Club club = clubService.findById(id);
        if (club == null) {
            return redirect(routes.Clubs.index());
        }

        ArrayList<Powerup> powerups = new ArrayList<>();
        club.setPowerups(powerups);
        // Sort the activations by weight:
        Collections.sort(club.getActivations(), new ActivationSorter());

        for (Activation activation : club.getActivations()) {
            Powerup powerup = activation.getPowerup();
            powerups.add(powerup);
        }
        return ok((Content) views.html.club.show.render(club));
    }

    public Result update() {
        final Map<String, String[]> postValues = request().body().asFormUrlEncoded();

        final Long id = Long.valueOf(postValues.get("id")[0]);
        final String newName = postValues.get("name")[0];
        final Club club = clubService.findById(id);

        club.setName(newName);
        //club.description = newDescription;

        Club.update(club);

        return redirect(routes.Clubs.index());
    }

    public Result create() {
        User user = userService.getCurrentUser(session());
        boolean authorized = userService.isUserAdmin(user);
        if (!authorized) {
            return unauthorized();
        }

        Map<String, String[]> form = request().body().asFormUrlEncoded();
        for (String key : form.keySet()) {
            Logger.warn(key + ":" + form.get(key)[0]);
        }
        String name = form.get("name")[0];
        String shortName = form.get("shortName")[0];
        String email = form.get("leader")[0] + form.get("postfix")[0];
        Long locationId = Long.valueOf(form.get("location")[0]);

        Location location = locationService.findById(locationId);

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
        club.getMembers().add(membership);

        ArrayList<Activation> activations = new ArrayList<>();
        powerupService.findAllMandatory().forEach(model -> {
            Activation activation = new Activation(club, model, model.getDefaultWeight());
            club.getActivations().add(activation);
            activations.add(activation);
        });

        membershipService.save(membership);

        for (Activation activation : activations) {
            activationService.save(activation);
            activation.getPowerup().activate();
        }
        return redirect(routes.Clubs.show(club.getId()));
    }


    public Result updatePowerup(Long clubId, Long powerupId) {
        JsonNode json = request().body().asJson();
        Logger.warn("RECEIVED JSON");
        if (json == null || json.isNull()) {
            return badRequest("Expecting Json data");
        }
        final Club club = clubService.findById(clubId);
        Powerup powerup = null;
        for (Activation activation : club.getActivations()) {
            if (activation.getPowerupModel().getId().equals(powerupId)) {
                powerup = activation.getPowerup();
            }
        }
        if (powerup == null) {
            return badRequest("No such powerup for " + club.getShortName());
        } else return powerup.update(json);
    }

    /**
     * WARNING: THIS RETURNS THE ADMIN FUNCTION
     */
    public Result getPowerupContent(Long clubId, Long powerupId) {
        final Club club = clubService.findById(clubId);

        Powerup powerup = null;

        if (club == null)
            return notFound((Content) views.html.index.render("Utvalget du leter etter finnes ikke."));

        for (Activation activation : club.getActivations()) {
            if (activation.getPowerupModel().getId().equals(powerupId)) {
                powerup = activation.getPowerup();
            }
        }
        if (powerup == null) {
            return notFound((Content) views.html.index.render("Poweruppen du leter etter finnes ikke for" + club.getShortName()));
        } else return ok(powerup.renderAdmin());
    }

}
