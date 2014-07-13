package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.Logger;
import play.Play;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import views.html.ckeditor.test;

import java.util.Map;

public class CKEditor extends Controller {

    public static Result test() {

        return ok(test.render(Play.application().path().getAbsolutePath()));
    }

    public static Result save() {

        final Map<String, String[]> map = request().body().asFormUrlEncoded();

        final String[] segments = map.get("editabledata");

        Logger.debug(map.get("context")[0]);

        StringBuilder builder = new StringBuilder();
        for(String s : segments) {builder.append(s);}

        Logger.debug("Content: " +builder.toString());

        return ok();
    }
}
