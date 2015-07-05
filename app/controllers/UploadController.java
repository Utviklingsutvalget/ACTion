package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import models.UploadedFile;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class UploadController extends Controller {

    public Result postFile() {
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart upload = body.getFile("file");
        if (upload != null) {
            UploadedFile attachment = new UploadedFile();
            String originalFileName = upload.getFilename();
            String[] split = originalFileName.split("\\.");
            String fileType = split[split.length - 1];

            StringBuilder builder = new StringBuilder();
            String hashedName = UUID.randomUUID().toString();
            builder.append(hashedName);
            builder.append(".").append(fileType);
            attachment.setFileName(builder.toString());

            File file = upload.getFile();
            if(file.length() > 5242880L) {
                return badRequest("Filen er for stor. Maks størrelse er 5MB");
            }


            Ebean.save(attachment);
            JsonNode jsonNode = Json.toJson(attachment);
            return ok(jsonNode);
        } else {
            return badRequest();
        }
    }
}
