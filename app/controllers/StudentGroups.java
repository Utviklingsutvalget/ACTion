package controllers;

import models.StudentGroup;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;
import java.util.Map;

public class StudentGroups extends Controller {
    public static Result index() {
        final List<StudentGroup> studentGroups = StudentGroup.find.all();
        return ok(views.html.studentgroup.index.render(studentGroups));
    }

    public static Result show(String id) {
        final Long studentGroupId = Long.valueOf(id);

        final StudentGroup studentGroup = StudentGroup.find.byId(studentGroupId);

        return ok(views.html.studentgroup.show.render(studentGroup));
    }

    public static Result update() {
        final Map<String, String[]> postValues = request().body().asFormUrlEncoded();

        final Long id = Long.valueOf(postValues.get("id")[0]);
        final String newName = postValues.get("name")[0];
        final String newDescription = postValues.get("description")[0];

        final StudentGroup studentGroup = StudentGroup.find.byId(id);

        studentGroup.name = newName;
        studentGroup.description = newDescription;

        StudentGroup.update(studentGroup);

        return redirect(routes.StudentGroups.index());
    }
}
