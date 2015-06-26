package controllers;

import models.InitiationGroup;
import models.Location;
import play.mvc.Controller;
import play.mvc.Result;
import services.InitiationGroupService;
import services.LocationService;
import utils.InitiationSorter;
import views.html.admin.initiation;

import javax.inject.Inject;
import java.util.List;

public class InitiationAdminController extends Controller {

    @Inject
    private InitiationGroupService initiationGroupService;
    @Inject
    private LocationService locationService;

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
        return ok(initiation.render(initiationGroups, locations, maxInitGrp));
    }

    public Result newSchedule() {
        return TODO;
    }
}
