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
import services.FileService;
import services.LocationService;
import services.UserService;
import views.html.admin.clubs.create;
import views.html.admin.clubs.edit;
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
    @Inject
    private FileService fileService;

    public Result update(Long id) {
        List<Location> locations = locationService.findAll();
        Form<Club> form = Form.form(Club.class).bindFromRequest(request());
        Map<String, String> data = form.data();
        Logger.warn("Received following data: " + data);
        discardError(form, "location");
        discardError(form, "uploadedFile");
        Club club = form.get();
        Club fromDb = clubService.findById(id);
        if(fromDb == null) {
            return notFound();
        }
        club.setId(id);

        String uploadedFileId = data.get("uploadedFile");
        club.setUploadedFile(fileService.getUploadedFileFromStringId(uploadedFileId));
        String locationId = data.get("location");
        club.setLocation(locationService.getLocationFromStringId(locations, locationId));

        User owner = club.getOwner();
        User userFromDb = userService.findByEmail(owner.getEmail());
        if(userFromDb == null) {
            ArrayList<ValidationError> value = new ArrayList<>();
            value.add(new ValidationError("owner", "Leder finnes ikke i systemet"));
            form.errors().put("owner", value);
        }

        if(form.hasErrors()) {
            return ok(edit.render(locations, form, id));
        }
        List<BoardMembership> boardMemberships = club.getBoardMemberships();
        assert userFromDb != null;
        if(!fromDb.getBoardMemberships().stream()
                .filter(boardMembership -> boardMembership.getUser()
                        .getEmail()
                        .equals(userFromDb.getEmail()))
                .findAny()
                .isPresent()) {
            boardMemberships.add(new BoardMembership(club, "Leder", userFromDb, 0));
        }
        club.setOwner(userFromDb);
        clubService.update(club);
        return redirect(routes.Clubs.show(club.getId()));
    }

    public Result edit(Long id) {
        Club club = clubService.findById(id);
        List<Location> locations = locationService.findAll();
        if(club == null) {
            return notFound();
        }
        Form<Club> form = Form.form(Club.class).fill(club);

        return ok(edit.render(locations, form, id));
    }

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
        }

        if(form.hasErrors()) {
            return ok(create.render(locations, form));
        }
        clubService.save(club);
        club.setOwner(fromDatabase);
        club.getBoardMemberships().add(new BoardMembership(club, "Leder", fromDatabase, 0));
        clubService.update(club);
        return redirect(routes.Clubs.show(club.getId()));
    }

    private void discardError(final Form<?> form, String field) {
        form.errors().remove(field);
    }

}
