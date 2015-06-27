package controllers;

import models.Location;
import models.clubs.Club;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.twirl.api.Content;
import services.ClubService;
import services.LocationService;
import views.html.admin.clubs.create;
import views.html.admin.clubs.index;

import javax.inject.Inject;
import java.util.List;

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
        return TODO;
    }
}
