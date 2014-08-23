package controllers;

import play.Logger;
import play.api.mvc.BodyParser;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Controller;
import play.mvc.Result;
import utils.ImageUploading;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Application extends Controller {

    public static final String FORMDATAFILEIDENTIFIER = "file";

    /**
     * intent is always the first field to be checked to see what type of image request is being sent.
     * NB:
     * make sure to check Imageuploading class in utils for instruction on usage of fieldnaming.
     * */

    public static Result index() {
        return Feeds.index();
    }

    public static Result uploadImage(){

        MultipartFormData body = request().body().asMultipartFormData();
        String ffff = "error";

        if(body != null){

            Map<String, String[]> extraStuff = body.asFormUrlEncoded();
            FilePart filePart = body.getFile(FORMDATAFILEIDENTIFIER);

            if(filePart != null){
                String fileName = filePart.getFilename();
                File file = filePart.getFile();

                Logger.debug("filename: " + fileName);
                ImageUploading imageUploading = new ImageUploading(extraStuff, file, fileName);
                ffff = imageUploading.getFileName();
            }



        }else{
            Logger.debug("body is null");
        }

        return ok("success, new image: " + ffff);
    }
}
