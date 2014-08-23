package controllers;

import models.UserImageFile;
import play.Logger;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Controller;
import play.mvc.Result;
import utils.imageuploading.UploadHandler;

import java.io.File;
import java.util.List;
import java.util.Map;

public class Application extends Controller {

    public static final String FORMDATAFILEIDENTIFIER = "file";

    /**
     * intent is always the first field to be checked to see what type of image request is being sent.
     * NB:
     * make sure to check Imageuploading class in utils for instruction on usage of fieldnaming.
     *
     * fix echoing of images in dropzone field from serverfolder
     * */

    public static Result index() {
        return Feeds.index();
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

                Logger.debug("filename: " + fileName);
                uploadHandler = new UploadHandler(extraStuff, file, fileName);
            }

        }else{
            Logger.debug("body is null");
        }

        return ok(uploadHandler.getReturnMessage());
    }

    public static Result getImages(){
        String currentUrl = request().getQueryString("url");
        Logger.debug(currentUrl);
        List<UserImageFile> userImageFiles = UserImageFile.find.all();

        File[] files = new File[userImageFiles.size()];

        for(int i = 0; i < userImageFiles.size(); i++){
            files[i] = new File(userImageFiles.get(i).imagePath);
        }

        return ok(files[0]);
    }
}
