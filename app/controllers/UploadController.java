package controllers;

import models.UploadedFile;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.FileService;

import javax.inject.Inject;

public class UploadController extends Controller {

    @Inject
    private FileService fileService;

    public Result postFile() {
        System.out.println("Receiving");
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart uploadFilePart = body.getFile("file");
        if (uploadFilePart != null) {
            System.out.println("Received");
            UploadedFile s3File = new UploadedFile();
            s3File.setName(uploadFilePart.getFilename());
            s3File.setFile(uploadFilePart.getFile());
            fileService.save(s3File);
            return ok(Json.toJson(s3File));
        } else {
            System.out.println("No reception");
            return badRequest("File upload error");
        }
    }
}

