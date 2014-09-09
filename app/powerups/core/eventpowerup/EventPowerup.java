package powerups.core.eventpowerup;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import models.*;
import models.Event;
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
import utils.imageuploading.ImageUpload;
import utils.imageuploading.WriteFiles;
import utils.imaging.ImageLinkValidator;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EventPowerup extends Powerup implements WriteFiles{

    public static final int MAX_EVENTS = 3;
    private static final String EVENT_DIR_IDENTIFIER = "event";

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

        if(updateContent == null || updateContent.isNull()){
            return Results.status(NO_UPDATE, "Manglende info fra feltene");
        }

        // TODO CHANGE DIMENSIONS IF NEEDED
        ImageLinkValidator validator = new ImageLinkValidator(new Dimension(500, 250), new Dimension(1000, 500), true);

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

        String fileName = getFileNameFromPath(coverUrl);

        //ImageLinkValidator.StatusMessage statusMessage = validator.validate(pictureUrl);
        ImageLinkValidator.StatusMessage statusMessage = validator.validate(ImageUpload.getFileFromDefaultDir(
                fileName
        ));

        if(statusMessage.isSuccess()){

            // bit weird implementation but we basically write to DB to get and id we then add to writeFile
            // then we fetch the event and change its coverurl.
            Event e = new Event(name, description, dateTime, location, coverUrl, this.getClub(), user);
            Ebean.save(e);

            Event insertedEvent = Event.find.byId(e.id);

            // writes file to serverdir and uses timestamp with event id
            String newUrl = writeFile(fileName, getClub().id.toString() + File.separator + EVENT_DIR_IDENTIFIER +
                    File.separator + e.id + new LocalDateTime());
            insertedEvent.coverUrl = newUrl;
            Ebean.save(insertedEvent);

            Participation p = new Participation(e, user);
            Ebean.save(p);

            return Results.ok("Event opprettet");
        }else{
            return Results.status(NO_UPDATE, statusMessage.getMessage());
        }
    }

    @Override
    public String getFileNameFromPath(String fileUrl) {
        // fetch imageName from imageUrl.
        String[] uploadedFileUrl = fileUrl.split("/");
        return uploadedFileUrl[uploadedFileUrl.length - 1];
    }

    @Override
    public String writeFile(String fileName, String subDir) {
        File f = ImageUpload.getFileFromDefaultDir(fileName);

        ImageUpload imageUpload = new ImageUpload(f, f.getName());
        imageUpload.writeFile(subDir, imageUpload.getFileName());

        String newUrl = imageUpload.returnFileUrl();

        ImageUpload.clearDefaultDir();
        return newUrl;
    }

    public boolean isUserPresent() {
        return userPresent;
    }

    public void setUserPresent(boolean userPresent) {
        this.userPresent = userPresent;
    }
}
