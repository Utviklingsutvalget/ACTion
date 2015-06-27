package controllers;

import models.Location;
import models.clubs.Club;
import play.Logger;
import play.data.Form;
import play.data.validation.ValidationError;
import play.mvc.Controller;
import play.mvc.Result;
import play.twirl.api.Content;
import services.ClubService;
import services.LocationService;
import views.html.admin.clubs.create;
import views.html.admin.clubs.index;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

public class ClubAdminController extends Controller {

    @Inject
    private ClubService clubService;

    @Inject
    private LocationService locationService;

    public Result index() {
        List<Location> locations = locationService.findAll();
        List<Club> clubs = clubService.findAll();
        return ok((Content) index.render(clubs, locations));
    }

    public Result startCreate() {
        Form<Club> form = Form.form(Club.class);
        List<Location> locations = locationService.findAll();

        return ok(create.render(locations, form));
    }

    public Result create() {
        List<Location> locations = locationService.findAll();
        Form<Club> form = Form.form(Club.class).bindFromRequest(request());
        Map<String, String> data = form.data();
        Logger.warn("Received following data: " + data);
        discardError(form, "location");
        Club club = form.get();

        String locationId = data.get("location");
        club.setLocation(getLocationFromStringId(locations, locationId));

        if(form.hasErrors()) {
            return ok(create.render(locations, form));
        }
        clubService.save(club);
        return redirect(routes.Clubs.show(club.getId()));
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
}
