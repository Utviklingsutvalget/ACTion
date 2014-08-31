package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.UserImageFile;
import play.Logger;
import play.libs.Json;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Controller;
import play.mvc.Result;
import utils.imageuploading.UploadHandler;
import utils.imageuploading.UserImageUpload;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Application extends Controller {

    public static final String FORMDATAFILEIDENTIFIER = "file";
    private static final String DELETE_TASK = "delete";
    private static final String UPLOAD_TASK = "upload";
    private static final String GET_TASK = "get";
    private static final String INTENTFIELD = "intent";
    private static final String USERIDFIELD = "userID";
    private static final String IMAGEIDFIELD = "imageID";
    private static final String CLUBIDFIELD = "clubID";
    private static final String FEEDIDFIELD = "feedID";

    /**
     * intent is always the first field to be checked to see what type of image request is being sent.
     * NB:
     * make sure to check Imageuploading class in utils for instruction on usage of fieldnaming.
     *
     *
     * */

    public static Result index() {
        return Feeds.index();
    }

    public static Result deleteUploadedImage(){

        String intent = request().getQueryString(INTENTFIELD);
        String userID = request().getQueryString(USERIDFIELD);
        String imageID = request().getQueryString(IMAGEIDFIELD);
        String clubID = request().getQueryString(CLUBIDFIELD);
        String feedID = request().getQueryString(FEEDIDFIELD);
        Logger.debug("intent: " + intent + ", userID: " + userID + ", imageID: " + imageID);

        UploadHandler uploadHandler = new UploadHandler(imageID, userID, intent, clubID, feedID, DELETE_TASK);

        return ok(uploadHandler.getReturnMessage());
    }

    public static Result uploadImage(){

        MultipartFormData body = request().body().asMultipartFormData();

        UploadHandler uploadHandler = null;

        if(body != null){

            Map<String, String[]> extraStuff = body.asFormUrlEncoded();
            FilePart filePart = body.getFile(FORMDATAFILEIDENTIFIER);

            if(filePart != null){
                String fileName = filePart.getFilename();
                File file = filePart.getFile();

                if(file == null){
                    return ok("No file selected");
                }

                Logger.debug("filename: " + fileName + ", file: " + file.toString());
                uploadHandler = new UploadHandler(extraStuff, file, fileName, UPLOAD_TASK);
            }

        }else{
            Logger.debug("body is null");
        }

        return ok(uploadHandler.getReturnMessage());
    }

    public static Result getImages(){
        String userId = request().getQueryString(USERIDFIELD);
        String intent = request().getQueryString(INTENTFIELD);
        String clubID = request().getQueryString(CLUBIDFIELD);
        String feedId = request().getQueryString(FEEDIDFIELD);

        // fetch an id and association to club/user based upon url.
        UploadHandler uploadHandler = new UploadHandler(intent, userId, clubID, feedId, GET_TASK);
        ObjectNode json = uploadHandler.getJson();

        Logger.debug(json.toString());

        return ok(json);
    }
}
