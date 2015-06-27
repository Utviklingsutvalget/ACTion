package controllers;

import com.google.inject.Inject;
import models.Location;
import models.Membership;
import models.User;
import models.clubs.Club;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.twirl.api.Content;
import services.ClubService;
import services.LocationService;
import services.MembershipService;
import services.UserService;

import java.util.List;
import java.util.Map;

public class Clubs extends Controller {

    @Inject
    private ClubService clubService;
    @Inject
    private UserService userService;
    @Inject
    private LocationService locationService;
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

        Membership membership = new Membership(club, leaderUser);
        club.getMembers().add(membership);

        return redirect(routes.Clubs.show(club.getId()));
    }

}
