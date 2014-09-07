package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.Logger;
import play.libs.Json;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Controller;
import play.mvc.Result;
import utils.imageuploading.ImageUpload;
import utils.imageuploading.UploadHandler;

import java.io.File;

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
     *
     * intent is always the first field to be checked to see what type of image request is being sent.
     * NB:
     * make sure to check Imageuploading class in utils for instruction on usage of fieldnaming.
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
        String uploadPath = "";

        if(body != null){

            FilePart filePart = body.getFile(FORMDATAFILEIDENTIFIER);

            if(filePart != null){
                String fileName = filePart.getFilename();
                File file = filePart.getFile();

                Logger.debug("file.getName(): " + file.getName() + ", filename: " + fileName);

                if(file == null){
                    return ok("No file selected");
                }

                Logger.debug("filename: " + fileName + ", file: " + file.toString());

                ImageUpload imageUpload = new ImageUpload(file, fileName);
                uploadPath = imageUpload.writeFileDefault();
                //uploadHandler = new UploadHandler(extraStuff, file, fileName, UPLOAD_TASK);
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
        /*
        String userId = request().getQueryString(USERIDFIELD);
        String intent = request().getQueryString(INTENTFIELD);
        String clubID = request().getQueryString(CLUBIDFIELD);
        String feedId = request().getQueryString(FEEDIDFIELD);
        */
        String fileName = request().getQueryString("fileName");

        /*
        Logger.debug("intent: " + intent);
        Logger.debug("clubid: " + clubID);
        Logger.debug("feedid: " + feedId);
        */
        Logger.debug("filename: " + fileName);

        // fetch an id and association to club/user based upon url.
        //UploadHandler uploadHandler = new UploadHandler(intent, userId, clubID, feedId, GET_TASK);
        String filePath = ImageUpload.checkForFileDefault(fileName);
        Logger.debug(filePath);
        ObjectNode outer = Json.newObject();
        ObjectNode inner = Json.newObject();
        inner.put("url", filePath);
        outer.put("file", inner);

        return ok(outer);
    }
}
