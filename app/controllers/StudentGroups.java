package controllers;

import models.StudentGroup;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

public class StudentGroups extends Controller {
    public static Result index() {
        List<StudentGroup> studentGroups = StudentGroup.find.all();
        return ok(views.html.group.index.render(studentGroups));
    }
}
