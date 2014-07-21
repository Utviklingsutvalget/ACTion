package controllers;

import models.Activation;
import models.Club;
import models.Membership;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;
import powerups.Powerup;
import utils.ActivationSorter;
import utils.Authorize;
import utils.MembershipLevel;
import views.html.club.admin.*;

import java.util.ArrayList;
import java.util.Collections;

public class Administration extends Controller {

    public static Result showClub(Long id) {
        Club club = Club.find.byId(id);
        club.powerups = new ArrayList<>();
        // Sort the activations by weight:
        Collections.sort(club.activations, new ActivationSorter());

        for(Activation activation : club.activations) {
            Powerup powerup = activation.getPowerup();
            club.powerups.add(powerup);
        }
        try {
            User user = new Authorize.UserSession().getUser();
            for(Membership mem : user.memberships) {
                if(mem.level == MembershipLevel.BOARD) {
                    return ok(views.html.club.admin.show.render(club));
                } else if(mem.club.equals(club) && mem.level.getLevel() >= MembershipLevel.BOARD.getLevel()) {
                    return ok(views.html.club.admin.show.render(club));
                }
            }
        } catch (Authorize.SessionException e) {
            return forbidden("fu");
        }
        return forbidden("fu");
    }

}
