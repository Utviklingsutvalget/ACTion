package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.i18n.phonenumbers.NumberParseException;
import helpers.UserService;
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

    public static Result showClub(Long id) {
        Club club = Club.find.byId(id);
        club.powerups = new ArrayList<>();
        // Sort the activations by weight:
        Collections.sort(club.activations, new ActivationSorter());

        for (Activation activation : club.activations) {
            Powerup powerup = activation.getPowerup();
            club.powerups.add(powerup);
        }
        try {
            User user = new Authorize.UserSession().getUser();
            for (Membership mem : user.getMemberships()) {
                if (user.isAdmin()) {
                    return ok(views.html.club.admin.show.render(club));
                } else if (mem.club.equals(club) && mem.level.getLevel() >= MembershipLevel.BOARD.getLevel()) {
                    return ok(views.html.club.admin.show.render(club));
                }
            }
        } catch (Authorize.SessionException e) {
            return forbidden(views.html.index.render("Du må være innlogget som administrator for å administrere siden"));
        }
        return forbidden(views.html.index.render("Du har ikke tilgang til å se denne siden."));
    }

    public static Result redirectToItsLearning(){
        return redirect(ITSLEARNINGREDIRECT);
    }

    public static Result showSite() {
        try {
            User user = new Authorize.UserSession().getUser();
            List<Club> clubList = Club.find.all();
            List<Location> locationList = Location.find.all();
            List<InitiationGroup> initiationGroups = InitiationGroup.find.all();
            initiationGroups.sort(new InitiationSorter());
            Integer maxInitGrp = 1;
            for (InitiationGroup initiationGroup : initiationGroups) {
                if (initiationGroup.getGroupNumber() > maxInitGrp) {
                    maxInitGrp = initiationGroup.getGroupNumber();
                }
            }
            clubList.remove(Club.find.byId(PRESIDING_COUNCIL_ID));

            if (user.isAdmin()) {
                return ok(views.html.admin.site.render(locationList, clubList, initiationGroups, maxInitGrp));
            }
        } catch (Authorize.SessionException e) {
            return forbidden(views.html.index.render("Du må være innlogget som administrator for å administrere siden"));
        }
        return forbidden(views.html.index.render("Du har ikke tilgang til å se denne siden."));
    }

    //@BodyParser.Of(BodyParser.Json.class)
    public static Result updateLocation() {

        JsonNode json = request().body().asJson();
        Map<Long, String> locationMap = new HashMap<>();
        List<Location> existingLocations = Location.find.all();

        try {

            User user = new Authorize.UserSession().getUser();

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

                    String locationName = locationMap.get(location.id);

                    if (locationName != null && !locationName.equals("") && !locationName.equals(location.name)) {

                        updateLocations(location.id, locationName);
                    }
                }
            }

        } catch (Authorize.SessionException e) {
            e.printStackTrace();
        }

        return ok();
    }

    // update LocationName to new name
    public static void updateLocations(Long locationId, String newLocationName) {

        Location location = Location.find.byId(locationId);

        location.name = newLocationName;
        Logger.info("updated locationId: " + location.id + ", new name: " + location.name);

        Ebean.save(location);
    }

    public static Result makeAdmin() {
        try {
            User user = new Authorize.UserSession().getUser();
            List<SuperUser> superUsers = SuperUser.find.all();
            if (superUsers.isEmpty()) {
                SuperUser superUser = new SuperUser(user);
                superUser.user = user;
                Ebean.save(superUser);
            }
        } catch (Authorize.SessionException e) {
            return notFound();
        }
        return Application.index();
    }

    public static Result deleteClub() {

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
                Club club = Club.find.byId(id);

                if (!club.id.equals(PRESIDING_COUNCIL_ID)) {

                    club.delete();
                    return ok("Utvalg slettet");
                }
            }
        }
        return badRequest("Sletting ble ikke foretatt");
    }

    public static Result addGuardian() {
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
        User guardian = UserService.findByEmail(email);
        Location location = Location.find.byId(locationId);
        String phoneNumber = form.get("phone")[0];

        if (guardian != null && location != null) {

            InitiationGroup initiationGroup = new InitiationGroup(guardian, location, groupNumber);
            try {
                initiationGroup.setPhoneNumber(phoneNumber);
            } catch (NumberParseException e) {
                return badRequest();
            }
            if (InitiationGroup.find.byId(initiationGroup.getId()) == null) {
                Ebean.save(initiationGroup);
            } else {
                Ebean.update(initiationGroup);
            }
        }

        return redirect(routes.Administration.showSite() + GUARDIANANCHOR);
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result deleteGuardian() {

        JsonNode json = request().body().asJson();

        Long locationId = json.get("location").asLong();
        String userId = json.get("guardian").asText();

        User guardian = UserService.findById(userId);
        Location location = Location.find.byId(locationId);

        InitiationGroup group = null;
        if (guardian != null && location != null) {
            group = InitiationGroup.find.byId(new InitiationGroup(guardian, location).getId());
        }

        if (group != null) {
            Ebean.delete(group);
            Logger.info("fant gruppen");
        }else{
            badRequest("Ingen faddergruppe funnet");
        }

        return ok("Fadder slettet");
        //return redirect(routes.Administration.showSite().url() + "#addguardian");
    }

    public static Result modifyGuardian() {
        return null;
    }
}
