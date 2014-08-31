package utils.imageuploading;

import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.routes;
import models.UserImageFile;
import play.Logger;
import play.api.Play;
import play.libs.Json;

import java.io.File;
import java.util.List;
import java.util.Map;

public class UploadHandler {

    private static final String PROFILE = "profilePicture";
    private static final String CLUB = "clubPicture";
    private static final String FEED = "feedPicture";
    private static final String INTENTFIELD = "intent";
    private static final String DELETE_TASK = "delete";
    private static final String UPLOAD_TASK = "upload";
    private static final String GET_TASK = "get";
    private static final String URLFIELD = "url";

    private String task;
    private String imageID;
    private String userID;
    private String clubID;
    private String feedID;
    private ObjectNode json;

    private ImageUpload imageUpload;
    private Map<String, String[]> paramsMap;
    private File file;
    private String fileName;
    private String returnMessage = "error";

    // uploading files
    public UploadHandler(Map<String, String[]> inputMap, File file, String fileName, String task) {
        setParamsMap(inputMap);
        setFile(file);
        setFileName(fileName);

        String intent = FieldFetch.getFieldValue(INTENTFIELD, getParamsMap());
        Logger.debug("intent: " + intent);

        taskResolve(task);
        intentResolve(intent);
    }

    // deleting existing files
    public UploadHandler(String imageID, String userID, String intent, String clubID, String feedID, String task) {
        setImageID(imageID);
        setUserID(userID);
        setClubID(clubID);
        setFeedID(feedID);
        taskResolve(task);
        intentResolve(intent);

    }

    // fetching files
    public UploadHandler(String intent, String userID, String clubID, String feedID, String task){
        json = Json.newObject();
        setUserID(userID);
        setClubID(clubID);
        setFeedID(feedID);
        taskResolve(task);
        intentResolve(intent);
    }

    private void taskResolve(String task){

        switch (task){
            case UPLOAD_TASK : setTask(UPLOAD_TASK);
                break;
            case DELETE_TASK : setTask(DELETE_TASK);
                break;
            case GET_TASK : setTask(GET_TASK);
                break;
            default: throw new IllegalArgumentException("task not recognised");
        }
    }

    private void intentResolve(String intent) {

        switch (intent){
            case PROFILE : profileHandler();
                break;
            case CLUB : clubHandler();
                break;
            case FEED : //method
                break;
            default: throw new IllegalArgumentException("intent matches none of identified types");
        }
    }

    private void profileHandler(){

        if(getTask().equals(DELETE_TASK)){

            setImageUpload(new UserImageUpload(getUserID(), getImageID()));

            UserImageUpload userImageUpload = (UserImageUpload) getImageUpload();
            userImageUpload.deleteProfilePicture();

            setReturnMessage(userImageUpload.getReturnMessage());

        }else if(getTask().equals(UPLOAD_TASK)){

            setImageUpload(new UserImageUpload(getParamsMap(), getFile(), getFileName()));

            UserImageUpload userImageUpload = (UserImageUpload) getImageUpload();
            userImageUpload.uploadProfilePicture();

            setReturnMessage(userImageUpload.getReturnMessage());

        }else{

            setJson(UserImageUpload.fetchJson(getUserID()));
            setReturnMessage("Here sum files yo");
        }
    }

    private void clubHandler(){

        if(getTask().equals(UPLOAD_TASK)){

            setImageUpload(new ClubImageUpload(getParamsMap(), getFile(), getFileName()));

            ClubImageUpload clubImageUpload = (ClubImageUpload) getImageUpload();
            clubImageUpload.uploadClubPicture();

            setReturnMessage(clubImageUpload.getReturnMessage());

        }else if(getTask().equals(DELETE_TASK)){

            setImageUpload(new ClubImageUpload(getClubID()));

            ClubImageUpload clubImageUpload = (ClubImageUpload) getImageUpload();
            clubImageUpload.deleteClubPicture();

            setReturnMessage(clubImageUpload.getReturnMessage());

        }else{

            setJson(ClubImageUpload.fetchJson(getClubID()));
        }
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {

        this.returnMessage = returnMessage;
    }

    public static File getFile(String subdir, String imagePath){
        return Play.getFile(subdir + File.separator + imagePath, Play.current());
    }

    public ImageUpload getImageUpload() {
        return imageUpload;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setParamsMap(Map<String, String[]> paramsMap) {
        this.paramsMap = paramsMap;
    }

    public void setImageUpload(ImageUpload imageUpload) {
        this.imageUpload = imageUpload;
    }

    public String getFileName() {
        return fileName;
    }

    public File getFile() {
        return file;
    }

    public Map<String, String[]> getParamsMap() {
        return paramsMap;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public ObjectNode getJson() {
        return json;
    }

    private void setJson(ObjectNode json) {
        this.json = json;
    }

    public String getFeedID() {
        return feedID;
    }

    public void setFeedID(String feedID) {
        this.feedID = feedID;
    }

    public String getClubID() {
        return clubID;
    }

    public void setClubID(String clubID) {
        this.clubID = clubID;
    }
}
