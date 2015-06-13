package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.inject.Inject;
import services.*;
import models.*;
import play.Logger;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import powerups.Powerup;
import utils.ActivationSorter;
import utils.Authorize;
import utils.InitiationSorter;
import utils.MembershipLevel;

import java.util.*;

public class Administration extends Controller {

    private static final String LOCATIONID = "locationId";
    private static final String LOCATIONNAME = "locationName";
    private static final String CONFIRM_DELETE = "confirmDelete";
    private static final String GUARDIANANCHOR = "#addguardian";
    private static final Long PRESIDING_COUNCIL_ID = 1L;
    private static final String ITSLEARNINGREDIRECT = "https://nith.itslearning.com/elogin/default.aspx";

    @Inject
    private ClubService clubService;
    @Inject
    private UserService userService;
    @Inject
    private InitiationGroupService initiationGroupService;
    @Inject
    private LocationService locationService;
    @Inject
    private MembershipService membershipService;
    @Inject
    private SuperUserService superUserService;

    public Result showClub(Long id) {
        Club club = clubService.findById(id);

        if (club == null)
            return notFound(views.html.index.render("Utvalget du leter etter finnes ikke."));


        ArrayList<Powerup> powerups = new ArrayList<>();
        club.setPowerups(powerups);
        // Sort the activations by weight:
        Collections.sort(club.getActivations(), new ActivationSorter());

        for (Activation activation : club.getActivations()) {
            Powerup powerup = activation.getPowerup();
            powerups.add(powerup);
        }
        try {
            User user = new Authorize.UserSession().getUser();
            Membership membership = membershipService.findById(new Membership(club, user).id);
            if (user.isAdmin() || membership.getLevel().getLevel() >= MembershipLevel.BOARD.getLevel()) {
                return ok(views.html.club.admin.show.render(club));
            } else {
                return forbidden(views.html.index.render("Du har ikke tilgang til å se denne siden."));
            }
        } catch (Authorize.SessionException e) {
            return forbidden(views.html.index.render("Du må være innlogget som administrator for å administrere siden"));
        }
    }

    public Result redirectToItsLearning() {
        return redirect(ITSLEARNINGREDIRECT);
    }

    public Result showSite() {
        try {
            User user = new Authorize.UserSession().getUser();
            List<Club> clubList = clubService.findAll();
            List<Location> locationList = locationService.findAll();
            List<InitiationGroup> initiationGroups = initiationGroupService.findAll();
            initiationGroups.sort(new InitiationSorter());
            Integer maxInitGrp = 1;
            for (InitiationGroup initiationGroup : initiationGroups) {
                if (initiationGroup.getGroupNumber() > maxInitGrp) {
                    maxInitGrp = initiationGroup.getGroupNumber();
                }
            }
            Club council = clubList.stream()
                    .filter(club -> club.getId().equals(PRESIDING_COUNCIL_ID))
                    .findFirst()
                    .get();
            clubList.remove(council);

            if (user.isAdmin()) {
                return ok(views.html.admin.site.render(locationList, clubList, initiationGroups, maxInitGrp));
            }
        } catch (Authorize.SessionException e) {
            return forbidden(views.html.index.render("Du må være innlogget som administrator for å administrere siden"));
        }
        return forbidden(views.html.index.render("Du har ikke tilgang til å se denne siden."));
    }

    //@BodyParser.Of(BodyParser.Json.class)
    public Result updateLocation() {

        JsonNode json = request().body().asJson();
        Map<Long, String> locationMap = new HashMap<>();
        List<Location> existingLocations = locationService.findAll();

        try {

            User user = new Authorize.UserSession().getUser();
            if(!user.isAdmin()) {
                return badRequest("Ingen tilgang");
            }

            if (json != null) {

                Iterator<String> iter = json.fieldNames();

                while (iter.hasNext()) {

                    String fieldName = iter.next();
                    String locationName = json.get(fieldName).asText();
                    Long locationId = Long.parseLong(fieldName);

                    //Logger.info("LocationName: " + locationName + ", LocationId: " + locationId.toString());
                    locationMap.put(locationId, locationName);
                }

                for (Location location : existingLocations) {

                    String locationName = locationMap.get(location.getId());

                    if (locationName != null && !locationName.equals("") && !locationName.equals(location.getName())) {

                        updateLocations(location.getId(), locationName);
                    }
                }
            }

        } catch (Authorize.SessionException e) {
            e.printStackTrace();
        }

        return ok("Lokasjon oppdatert");
    }

    // update LocationName to new name
    public void updateLocations(Long locationId, String newLocationName) {

        Location location = locationService.findById(locationId);

        location.setName(newLocationName);
        Logger.info("updated locationId: " + location.getId() + ", new name: " + location.getName());

        locationService.update(location);
    }

    public Result makeAdmin() {
        try {
            User user = new Authorize.UserSession().getUser();
            List<SuperUser> superUsers = superUserService.findAll();
            if (superUsers.isEmpty()) {
                SuperUser superUser = new SuperUser(user);
                superUser.setUser(user);
                Ebean.save(superUser);
            }
        } catch (Authorize.SessionException e) {
            return notFound();
        }
        // TODO MAKE SENSE
        return redirect("/");
    }

    public Result deleteClub() {

        JsonNode json = request().body().asJson();
        Map<Long, String> clubMap = new HashMap<>();
        Map<String, String> confirmDelMap = new HashMap<>();
        Iterator<String> iter = json.fieldNames();

        while (iter.hasNext()) {

            String key = iter.next();
            String val = json.get(key).asText();

            if (key.equals(CONFIRM_DELETE)) {

                confirmDelMap.put(key, val);

            } else {
                Long newVal = Long.parseLong(val);
                clubMap.put(newVal, key);
            }

            Logger.info("At the end of loop, key: " + key + ", val: " + val);
        }

        // check for
        for (Long id : clubMap.keySet()) {

            if (confirmDelMap.get(CONFIRM_DELETE).equals(clubMap.get(id))) {
                Club club = clubService.findById(id);

                if (!club.getId().equals(PRESIDING_COUNCIL_ID)) {

                    club.delete();
                    return ok("Utvalg slettet");
                }
            }
        }
        return badRequest("Sletting ble ikke foretatt");
    }

    public Result addGuardian() {
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
        String email = form.get("guardian")[0] + form.get("postfix")[0];
        int groupNumber = Integer.valueOf(form.get("group-number")[0]);
        Long locationId = Long.valueOf(form.get("location")[0]);
        User guardian = userService.findByEmail(email);
        Location location = locationService.findById(locationId);
        String phoneNumber = form.get("phone")[0];

        if (guardian != null && location != null) {

            InitiationGroup initiationGroup = new InitiationGroup(guardian, location, groupNumber);
            try {
                initiationGroup.setPhoneNumber(phoneNumber);
            } catch (NumberParseException e) {
                return badRequest();
            }
            if (initiationGroupService.findById(initiationGroup.getId()) == null) {
                Ebean.save(initiationGroup);
            } else {
                Ebean.update(initiationGroup);
            }
        }

        return redirect(routes.Administration.showSite() + GUARDIANANCHOR);
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result deleteGuardian() {

        JsonNode json = request().body().asJson();

        Long locationId = json.get("location").asLong();
        String userId = json.get("guardian").asText();

        User guardian = userService.findById(userId);
        Location location = locationService.findById(locationId);

        InitiationGroup group = null;
        if (guardian != null && location != null) {
            group = initiationGroupService.findById(new InitiationGroup(guardian, location).getId());
        }

        if (group != null) {
            Ebean.delete(group);
            Logger.info("fant gruppen");
        } else {
            badRequest("Ingen faddergruppe funnet");
        }

        return ok("Fadder slettet");
        //return redirect(routes.Administration.showSite().url() + "#addguardian");
    }

    public Result modifyGuardian() {
        return null;
    }
}
