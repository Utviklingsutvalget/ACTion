package controllers;

import models.InitiationEvent;
import models.InitiationGroup;
import models.InitiationSchedule;
import models.Location;
import play.data.Form;
import play.data.validation.ValidationError;
import play.mvc.Controller;
import play.mvc.Result;
import play.twirl.api.Content;
import services.InitiationEventService;
import services.InitiationGroupService;
import services.InitiationScheduleService;
import services.LocationService;
import utils.InitiationSorter;
import views.html.admin.initiation.create;
import views.html.admin.initiation.index;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static play.data.Form.form;

public class InitiationAdminController extends Controller {

    @Inject
    private InitiationScheduleService initiationScheduleService;
    @Inject
    private InitiationGroupService initiationGroupService;
    @Inject
    private LocationService locationService;
    @Inject
    private InitiationEventService initiationEventService;

    public Result index() {
        List<Location> locations = locationService.findAll();
        List<InitiationGroup> initiationGroups = initiationGroupService.findAll();
        initiationGroups.sort(new InitiationSorter());
        Integer maxInitGrp = 1;
        for (InitiationGroup initiationGroup : initiationGroups) {
            if (initiationGroup.getGroupNumber() > maxInitGrp) {
                maxInitGrp = initiationGroup.getGroupNumber();
            }
        }
        return ok((Content) index.render(initiationGroups, locations, maxInitGrp));
    }

    public Result newSchedule() {
        Form<InitiationSchedule> form = form(InitiationSchedule.class);
        List<Location> locations = locationService.findAll();

        return ok(create.render(locations, form));
    }

    public Result createSchedule() {
        List<Location> locations = locationService.findAll();
        Form<Location> form = form(Location.class).bindFromRequest(request());

        Map<String, String> data = form.data();
        String locationId = data.get("location");   // BUG: IF "FELLES" NumberFormatException IS THROWN

        InitiationSchedule initiationSchedule = new InitiationSchedule();
        initiationSchedule.setYear(Year.now());
        initiationSchedule.setCampus(getLocationFromStringId(locations, locationId));

        initiationScheduleService.save(initiationSchedule);
        return redirect(routes.InitiationAdminController.newEvent(initiationSchedule.getId()));
    }

    public Result newEvent(long scheduleId) {
        Form<InitiationEvent> form = form(InitiationEvent.class);

        return ok((Content) views.html.admin.initiation.events.create.render(form, scheduleId));
    }

    public Result createEvent(long scheduleId) {
        Form<InitiationEvent> form = form(InitiationEvent.class).bindFromRequest(request());
        Map<String, String> data = form.data();

        InitiationEvent initiationEvent = new InitiationEvent(data.get("title"), getLocalDateTimeFromString(data.get("date")), data.get("location"),
                data.get("description"));

        initiationEventService.save(initiationEvent);

        return redirect(routes.InitiationAdminController.showEvents(scheduleId));
    }

    public Result showEvents(long scheduleId) {
        InitiationSchedule initiationSchedule = initiationScheduleService.findScheduleById(scheduleId);
        List<InitiationEvent> initiationEvents = initiationEventService.findAll();

        return ok((Content) views.html.admin.initiation.events.show.render(initiationSchedule, initiationEvents));
    }

    public Result deleteEvent(long initiationEventId) {
        /*initiationEventService.delete(initiationEventId);*/
        return TODO;
    }


    private void discardError(final Form<?> form, String field) {
        List<ValidationError> location = form.errors().remove(field);
    }

    private Location getLocationFromStringId(final List<Location> locations, final String locationId) {
        return locations.stream()
                .filter(loc -> loc.getId() == Long .parseLong(locationId))
                .findFirst()
                .orElseGet(null);
    }

    private LocalDateTime getLocalDateTimeFromString(String localDateTimeAsString) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

        return LocalDateTime.parse(localDateTimeAsString, dateTimeFormatter);
    }
}
