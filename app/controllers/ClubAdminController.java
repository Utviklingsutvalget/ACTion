package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import play.twirl.api.Content;
import views.html.admin.clubs.index;

public class ClubAdminController extends Controller {

    public Result index() {
        return ok((Content) index.render());
    }
}
