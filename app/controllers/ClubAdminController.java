package controllers;

import models.Location;
import models.User;
import models.clubs.BoardMembership;
import models.clubs.Club;
import play.Logger;
import play.data.Form;
import play.data.validation.ValidationError;
import play.mvc.Controller;
import play.mvc.Result;
import play.twirl.api.Content;
import services.ClubService;
import services.LocationService;
import services.UserService;
import views.html.admin.clubs.create;
import views.html.admin.clubs.index;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ClubAdminController extends Controller {

    @Inject
    private ClubService clubService;

    @Inject
    private LocationService locationService;
    @Inject
    private UserService userService;

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
        club.setLocation(locationService.getLocationFromStringId(locations, locationId));

        User owner = club.getOwner();
        User fromDatabase = userService.findByEmail(owner.getEmail());
        if(fromDatabase == null) {
            ArrayList<ValidationError> value = new ArrayList<>();
            value.add(new ValidationError("owner", "Leder finnes ikke i systemet"));
            form.errors().put("owner", value);
        } else {
            club.setOwner(fromDatabase);
            club.getBoardMemberships().add(new BoardMembership(club, "Leder", fromDatabase, 0));
        }

        if(form.hasErrors()) {
            return ok(create.render(locations, form));
        }
        clubService.save(club);
        return redirect(routes.Clubs.show(club.getId()));
    }

    private void discardError(final Form<?> form, String field) {
        form.errors().remove(field);
    }

}
