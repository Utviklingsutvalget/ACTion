package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.Logger;
import play.libs.Json;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Controller;
import play.mvc.Result;
import utils.imageuploading.ImageUpload;

import java.io.File;

public class Application extends Controller {

    public static final String FORMDATAFILEIDENTIFIER = "file";

    /**
     *
     * intent is always the first field to be checked to see what type of image request is being sent.
     * NB:
     * make sure to check Imageuploading class in utils for instruction on usage of fieldnaming.
     *
     * */

    public static Result index() {
        return Feeds.index();
    }

    public static Result uploadImage(){

        MultipartFormData body = request().body().asMultipartFormData();
        String uploadPath = "";

        if(body != null){

            FilePart filePart = body.getFile(FORMDATAFILEIDENTIFIER);

            if(filePart != null){
                String fileName = filePart.getFilename();
                File file = filePart.getFile();

                Logger.debug("file.getName(): " + file.getName() + ", filename: " + fileName);
                Logger.debug("filename: " + fileName + ", file: " + file.toString());

                ImageUpload imageUpload = new ImageUpload(file, fileName);
                uploadPath = imageUpload.writeFileDefault();
            }

        }else{
            uploadPath = "error";
            Logger.debug("body is null");
        }

        ObjectNode outer = Json.newObject();
        ObjectNode inner = Json.newObject();
        inner.put("url", uploadPath);
        outer.put("file", inner);

        return ok(outer);
    }

    public static Result getImages(){

        String fileName = request().getQueryString("fileName");
        Logger.debug("filename: " + fileName);

        String filePath = ImageUpload.checkAndReturnDefaultDirFileUrl(fileName);

        Logger.debug(filePath);
        ObjectNode outer = Json.newObject();
        ObjectNode inner = Json.newObject();
        inner.put("url", filePath);
        outer.put("file", inner);

        return ok(outer);
    }
}
