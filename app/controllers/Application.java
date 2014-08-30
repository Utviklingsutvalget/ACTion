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


    /**
     * intent is always the first field to be checked to see what type of image request is being sent.
     * NB:
     * make sure to check Imageuploading class in utils for instruction on usage of fieldnaming.
     *
     *
     * */

     /*
    * TODO
    *
    * attach imageID to button so that each button knows its own imageID for deletion. (done)
    * remove all references to dropzone.js
    *
    * fix backend so that UploadHandler and imageupload dont depend too much on paramsmap, rather
    * send info to their constructors.
    *
    * */
    public static Result index() {
        return Feeds.index();
    }

    public static Result deleteUploadedImage(){

        String intent = request().getQueryString("intent");
        String userID = request().getQueryString("userID");
        String imageID = request().getQueryString("imageID");
        Logger.debug("intent: " + intent + ", userID: " + userID + ", imageID: " + imageID);

        UploadHandler uploadHandler = new UploadHandler(imageID, userID, intent, DELETE_TASK);


        // resolve the intent and then call appropriate uploadClass

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

                Logger.debug("filename: " + fileName + ", file: " + file.toString());
                uploadHandler = new UploadHandler(extraStuff, file, fileName, UPLOAD_TASK);
            }

        }else{
            Logger.debug("body is null");
        }

        return ok(uploadHandler.getReturnMessage());
    }

    public static Result getImages(){
        Logger.debug("getImages called!");
        String userId = request().getQueryString("userID");
        String intent = request().getQueryString("intent");
        String clubID = request().getQueryString("clubID");
        String feedId = request().getQueryString("feedID");

        // fetch an id and association to club/user based upon url.
        UploadHandler uploadHandler = new UploadHandler(intent, userId, clubID, feedId, GET_TASK);
        ObjectNode json = uploadHandler.getJson();

        Logger.debug(json.toString());

        return ok(json);

    }
}
