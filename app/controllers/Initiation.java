package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import models.InitiationGroup;
import models.Location;
import play.Logger;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Initiation extends Controller {

    // We expect the search function to be used while drunk.
    public static final String[] GROUP_STRINGS = {
            "gruppe", "group", "grp", "grpp", "grupp", "grop", "grup", "gruppee", "gruppr", "gruppw", "gryppe", "gruup", "feuppe"
    };

    // As above. Accidents around the enter button happen.
    public static final char[] UNWANTED_CHARACTERS = {
            ':', ';', '.', ',', '!', '?'
    };

    public static Result index() {
        return ok(views.html.initiation.index.render());
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result search() {
        JsonNode json = request().body().asJson();
        String query = json.get("query").asText().toLowerCase();
        Long locationId = json.get("location").asLong();
        Location location = Location.find.byId(locationId);

        if (location == null) {
            return notFound("Feil oppsett på server, vi støtter dessverre ikke denne lokasjonen. Vennligst kontakt Eivind på" +
                    "95202268");
        }


        Map<Integer, List<InitiationGroup>> groupMap = new HashMap<>();
        List<InitiationGroup> list;
        // If location turns up null, let's not panic. We'll just be vague.

        for (char c : UNWANTED_CHARACTERS) {
            query = query.replace(String.valueOf(c), "");
        }

        try {
            // Try dealing with the query as a group.
            String queryCopy = query;

            // Remove all instances of variations of "group" in the query, as we want a pure integer
            for (String sequence : GROUP_STRINGS) {
                queryCopy = queryCopy.replace(sequence, "");
            }
            // Get rid of all spaces.
            queryCopy = queryCopy.trim();
            final Integer groupNumber = Integer.parseInt(queryCopy);
            list = Ebean.find(InitiationGroup.class).where().eq("groupNumber", groupNumber).eq("location", location).findList();

            if (!list.isEmpty()) {
                groupMap.put(groupNumber, list);
            }
        } catch (NumberFormatException e) {
            // Assume the query is for a name, not a specific group!
            // Get rid of leading or trailing spaces.
            String queryCopy = query.trim();

            List<InitiationGroup> tempList;
            tempList = Ebean.find(InitiationGroup.class).where().eq("location", location).findList();

            for (InitiationGroup initGroup : tempList) {
                String guardianName = (initGroup.getGuardian().getFirstName() + " " + initGroup.getGuardian().getLastName()).toLowerCase();
                if (guardianName.contains(queryCopy)) {
                    int groupNumber = initGroup.getGroupNumber();
                    // Checking if the group already has a list, if not, set up the key/value pair
                    if (!groupMap.containsKey(groupNumber)) {
                        List<InitiationGroup> mapList = new ArrayList<>();
                        for(InitiationGroup initiationGroup : tempList) {
                            if(initiationGroup.getGroupNumber() == groupNumber) {
                                mapList.add(initiationGroup);
                            }
                        }
                        //List<InitiationGroup> mapList = Ebean.find(InitiationGroup.class).where().eq("groupNumber", groupNumber).eq("location", location).findList();
                        groupMap.put(groupNumber, mapList);
                    }
                }
            }
        }
        return ok(views.html.initiation.search.render(query, groupMap));
    }
}
