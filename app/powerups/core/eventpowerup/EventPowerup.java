package powerups.core.eventpowerup;

import com.fasterxml.jackson.databind.JsonNode;
import models.Club;
import models.Event;
import models.PowerupModel;
import models.User;
import play.Logger;
import play.mvc.Result;
import play.twirl.api.Html;
import powerups.Powerup;
import utils.EventSorter;
import powerups.core.eventpowerup.html.powerup;

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

        Logger.warn("Hei");
        List<Event> tempEvents = getClub().events;
        tempEvents.sort(new EventSorter());
        Logger.warn("Hei");
        tempEvents.stream().filter(e -> e.startTime.getTime() < new Date().getTime()).forEach(tempEvents::remove);
        events = new ArrayList<>();
        Logger.warn("Hei");
        Logger.warn("Neste er teit");
        if(tempEvents.size() < MAX_EVENTS) {
            events.addAll(tempEvents);
        } else if(!tempEvents.isEmpty()) {
            events.addAll(tempEvents.subList(0, MAX_EVENTS-1));
        }
        Logger.warn("Forrige var ikke teit");
        Logger.warn("Hei");
        User user = getContext().getSender();
        this.setUserPresent(user != null);
        Logger.warn("Hei");
    }

    @Override
    public Html render() {
        return powerup.render(events, isUserPresent());
    }

    @Override
    public void activate() {

    }

    @Override
    public Result update(JsonNode updateContent) {
        return null;
    }

    public boolean isUserPresent() {
        return userPresent;
    }

    public void setUserPresent(boolean userPresent) {
        this.userPresent = userPresent;
    }
}
