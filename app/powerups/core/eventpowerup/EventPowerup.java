package powerups.core.eventpowerup;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import controllers.Clubs;
import models.Club;
import models.Event;
import models.PowerupModel;
import models.User;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import play.Logger;
import play.mvc.Result;
import play.mvc.Results;
import play.twirl.api.Html;
import powerups.Powerup;
import powerups.core.eventpowerup.html.admin;
import powerups.core.eventpowerup.html.powerup;
import utils.EventSorter;
import utils.MembershipLevel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventPowerup extends Powerup {

    public static final int MAX_EVENTS = 3;

    private List<Event> events;
    private boolean userPresent;

    public EventPowerup(Club club, PowerupModel model) {
        super(club, model);
        events = new ArrayList<>();

        Logger.warn("Didn't fail yet1");
        List<Event> tempEvents = getClub().events;
        tempEvents.sort(new EventSorter());
        Logger.warn("Didn't fail yet2");
        tempEvents.stream().filter(e -> e.startTime.getTime() < new Date().getTime()).forEach(tempEvents::remove);
        events = new ArrayList<>();
        Logger.warn("Didn't fail yet3");
        if(tempEvents.size() < MAX_EVENTS) {
            events.addAll(tempEvents);
        } else if(!tempEvents.isEmpty()) {
            events.addAll(tempEvents.subList(0, MAX_EVENTS-1));
        }
        Logger.warn("Didn't fail yet4");
        User user = getContext().getSender();
        this.setUserPresent(user != null);
    }

    @Override
    public Html render() {
        return powerup.render(events, isUserPresent());
    }

    @Override
    public Html renderAdmin() {
        return admin.render(events);
    }

    @Override
    public void activate() {

    }

    @Override
    public Result update(JsonNode updateContent) {
        User user = getContext().getSender();
        if(!(user.isAdmin() || getContext().getMemberLevel().getLevel() >= MembershipLevel.BOARD.getLevel())) {
            return Results.unauthorized();
        }
        String name = updateContent.get("name").asText();
        String description = updateContent.get("description").asText();
        String location = updateContent.get("location").asText();
        String time = updateContent.get("time").asText();
        String coverUrl = updateContent.get("imagelink").asText();
        String[] timeSplits = time.split("T");

        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");

        DateTime dateTime = format.parseDateTime(timeSplits[0] + " " + timeSplits[1]);
        Date startTime = dateTime.toDate();

        Event e = new Event(name, description, startTime, location, coverUrl, this.getClub());
        Ebean.save(e);
        return Clubs.show(this.getClub().id);
    }

    public boolean isUserPresent() {
        return userPresent;
    }

    public void setUserPresent(boolean userPresent) {
        this.userPresent = userPresent;
    }
}
