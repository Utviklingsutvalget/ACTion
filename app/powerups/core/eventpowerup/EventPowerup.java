package powerups.core.eventpowerup;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import models.*;
import org.joda.time.LocalDateTime;
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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EventPowerup extends Powerup {

    public static final int MAX_EVENTS = 3;

    private List<Event> events;
    private boolean userPresent;

    public EventPowerup(Club club, PowerupModel model) {
        super(club, model);
        events = new ArrayList<>();

        List<Event> tempEvents = getClub().events;
        tempEvents.sort(new EventSorter());
        events.addAll(tempEvents.stream().filter(e -> e.startTime.isAfter(LocalDateTime.now().minusHours(3))).collect(Collectors.toList()));
        if(events.size() > MAX_EVENTS) {
            for(int i = MAX_EVENTS; i < events.size() ; i++) {
                events.remove(i);
            }
        }
        for (Event e : events) {
            Participation participation = new Participation(e, this.getContext().getSender());
            int i;
            if (e.participants.contains(participation)) {
                i = e.participants.indexOf(participation);
                participation = e.participants.get(i);
                e.setUserHosting(participation.rvsp == Participation.Status.HOSTING);
                e.setUserAttending(participation.getRvsp());
            }
            e.setUserAttending(participation.getRvsp());
            User user = getContext().getSender();
            this.setUserPresent(user != null);
        }
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
    public void deActivate() {

    }

    @Override
    public Result update(JsonNode updateContent) {
        User user = getContext().getSender();
        if (!(user.isAdmin() || getContext().getMemberLevel().getLevel() >= MembershipLevel.BOARD.getLevel())) {
            return Results.unauthorized("Ingen tilgang til å opprette events for " + this.getClub().name);
        }
        String name = updateContent.get("name").asText();
        String description = updateContent.get("description").asText();
        String location = updateContent.get("location").asText();
        String time = updateContent.get("time").asText();
        String coverUrl = updateContent.get("imagelink").asText();

        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy/MM/dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(time, format);
        if (dateTime.isBefore(LocalDateTime.now())) {
            return Results.unauthorized("Kan ikke opprette events i fortiden.");
        }

        Event e = new Event(name, description, dateTime, location, coverUrl, this.getClub(), user);
        Ebean.save(e);

        Participation p = new Participation(e, user);
        Ebean.save(p);

        return Results.ok("Event opprettet");
    }

    public boolean isUserPresent() {
        return userPresent;
    }

    public void setUserPresent(boolean userPresent) {
        this.userPresent = userPresent;
    }
}
