package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import models.*;
import play.Logger;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import powerups.Powerup;
import powerups.models.BoardMembership;
import powerups.models.ClubDescription;
import powerups.models.ClubImage;
import powerups.models.Pending;
import utils.ActivationSorter;
import utils.Authorize;
import utils.MembershipLevel;

import java.util.*;

public class Administration extends Controller {

    private static final String LOCATIONID = "locationId";
    private static final String LOCATIONNAME = "locationName";
    private static final String CONFIRMDELETE = "confirmDelete";
    private static final Long RESIDINGCOUNCILID = new Long(1);

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
            for (Membership mem : user.memberships) {
                if (user.isAdmin()) {
                    return ok(views.html.club.admin.show.render(club));
                } else if (mem.club.equals(club) && mem.level.getLevel() >= MembershipLevel.BOARD.getLevel()) {
                    return ok(views.html.club.admin.show.render(club));
                }
            }
        } catch (Authorize.SessionException e) {
            return forbidden("fu");
        }
        return forbidden("fu");
    }

    public static Result showSite() {
        try {
            User user = new Authorize.UserSession().getUser();
            List<Club> clubList = Club.find.all();
            List<Location> locationList = Location.find.all();
            clubList.remove(Club.find.byId(RESIDINGCOUNCILID));

            if(user.isAdmin()) {
                return ok(views.html.admin.site.render(locationList, clubList));
            }
        } catch (Authorize.SessionException e) {
            return forbidden("fu");
        }
        return forbidden("fu");
    }

    //@BodyParser.Of(BodyParser.Json.class)
    public static Result updateLocation() {

        JsonNode json = request().body().asJson();
        Map<Long, String> locationMap = new HashMap<>();
        List<Location> existingLocations = Location.find.all();

        try{

            User user = new Authorize.UserSession().getUser();

            if(json != null){

                Iterator<String> iter = json.fieldNames();

                while(iter.hasNext()){

                    String fieldName = iter.next();
                    String locationName = json.get(fieldName).asText();
                    Long locationId = Long.parseLong(fieldName);

                    //Logger.info("LocationName: " + locationName + ", LocationId: " + locationId.toString());
                    locationMap.put(locationId, locationName);
                }

                for(Location location : existingLocations){

                    String locationName = locationMap.get(location.id);

                    if(locationName != null && !locationName.equals("") && !locationName.equals(location.name)){

                        updateLocations(location.id, locationName);
                    }
                }
            }

        }catch(Authorize.SessionException e){
            e.printStackTrace();
        }

        return ok();
    }

    // update LocationName to new name
    public static void updateLocations(Long locationId, String newLocationName){

        Location location = Location.find.byId(locationId);

        location.name = newLocationName;
        Logger.info("updated locationId: " + location.id + ", new name: " + location.name);

        Ebean.save(location);
    }

    public static Result makeAdmin() {
        try {
            User user = new Authorize.UserSession().getUser();
            List<SuperUser> superUsers = SuperUser.find.all();
            if(superUsers.isEmpty()) {
                SuperUser superUser = new SuperUser(user);
                superUser.user = user;
                Ebean.save(superUser);
            }
        } catch (Authorize.SessionException e) {
            return notFound();
        }
        return Application.index();
    }

    public static Result deleteClub(){

        JsonNode json = request().body().asJson();
        Map<Long, String> clubMap = new HashMap<>();
        Map<String, String> confirmDelMap = new HashMap<>();
        Iterator<String> iter = json.fieldNames();

        while(iter.hasNext()){

            String key = iter.next();
            String val = json.get(key).asText();

            if(key.equals(CONFIRMDELETE)){

                confirmDelMap.put(key, val);

            }else{
                Long newVal = Long.parseLong(val);
                clubMap.put(newVal, key);
            }

            Logger.info("At the end of loop, key: " + key + ", val: " + val);
        }

        // check for
        for(Long id : clubMap.keySet()){

            if(confirmDelMap.get(CONFIRMDELETE).equals(clubMap.get(id))){
                Club club = Club.find.byId(id);

                if(!club.id.equals(RESIDINGCOUNCILID)){

                    deleteAllConnections(club);

                    Ebean.delete(club);
                    return ok("Utvalg slettet");
                }
            }
        }
        return badRequest("Sletting ble ikke foretatt");
    }

    private static void deleteAllConnections(Club club){
        clearList(club.activations);
        clearList(club.boardMembers);
        clearImage(club);
        clearList(club.members);
        clearList(Pending.getByClubId(club.id));
        clearList(Feed.findByClub(club));
        removeParticipations(club);
        clearList(club.events);
        clearDescription(club);
    }

    private static void removeParticipations(Club club){

        for(Event event : club.events){
            clearList(event.participants);
        }
    }

    private static void clearDescription(Club club){
        ClubDescription clubDescription = ClubDescription.find.byId(club.id);

        if(clubDescription != null){
            Ebean.delete(clubDescription);
            Logger.info("deleted clubdescription");
        }else{
            Logger.info("found no Clubdescription");
        }
    }

    private static void clearImage(Club club){
        ClubImage clubImage = ClubImage.getImageByClub(club);

        if(clubImage != null){
            Ebean.delete(clubImage);
            Logger.info("Deleted image from club");
        }else{
            Logger.info("found no clubImage");
        }
    }

    private static void clearList(List list){
        if(list != null){
            Logger.info("deleted list from club.");
            Ebean.delete(list);
        }else{
            Logger.info("list is either empty or null");
        }
    }
}
