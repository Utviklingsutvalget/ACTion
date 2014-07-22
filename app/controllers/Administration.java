package controllers;

import com.avaje.ebean.Ebean;
import models.*;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import powerups.Powerup;
import utils.ActivationSorter;
import utils.Authorize;
import utils.MembershipLevel;

import java.util.*;

public class Administration extends Controller {

    public static Result showClub(Long id) {
        Club club = Club.find.byId(id);
        club.powerups = new ArrayList<>();
        // Sort the activations by weight:
        Collections.sort(club.activations, new ActivationSorter());

        for (Activation activation : club.activations) {
            Powerup powerup = activation.getPowerup();
            club.powerups.add(powerup);
        }
        try {
            User user = new Authorize.UserSession().getUser();
            for (Membership mem : user.memberships) {
                if (user.isAdmin()) {
                    return ok(views.html.club.admin.show.render(club));
                } else if (mem.club.equals(club) && mem.level.getLevel() >= MembershipLevel.BOARD.getLevel()) {
                    return ok(views.html.club.admin.show.render(club));
                }
            }
        } catch (Authorize.SessionException e) {
            return forbidden("fu");
        }
        return forbidden("fu");
    }

    public static Result showSite() {
        try {
            User user = new Authorize.UserSession().getUser();
            List<Location> locationList = Location.find.all();
            if(user.isAdmin()) {
                return ok(views.html.admin.site.render(locationList));
            }
        } catch (Authorize.SessionException e) {
            return forbidden("fu");
        }
        return forbidden("fu");
    }

    public static Result updateLocation() {
        return null;
    }

    public static Result makeAdmin() {
        try {
            User user = new Authorize.UserSession().getUser();
            List<SuperUser> superUsers = SuperUser.find.all();
            if(superUsers.isEmpty()) {
                SuperUser superUser = new SuperUser(user);
                superUser.user = user;
                Ebean.save(superUser);
            }
        } catch (Authorize.SessionException e) {
            return notFound();
        }
        return Application.index();
    }

}
