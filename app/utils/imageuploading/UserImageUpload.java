package utils.imageuploading;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.routes;
import models.User;
import models.UserImageFile;
import play.Logger;
import play.api.Play;
import play.libs.Json;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserImageUpload extends ImageUpload {

    private static final String USERPATH = "users";
    private static final String USERIDFIELD = "userID";
    private static final int MAXUSERIMAGES = 5;
    private User user;
    private String returnMessage = "error";


    public UserImageUpload(Map<String, String[]> inputMap, File file, String fileName) {
        super(file, fileName);

        User user = checkForValidUser(FieldFetch.getFieldValue(USERIDFIELD, inputMap));
        setUser(user);
    }

    public UserImageUpload(String userID, String imageID){
        User user = checkForValidUser(userID);
        setUser(user);

        Long foundImageID = Long.parseLong(imageID);
        UserImageFile userImageFile = UserImageFile.find.byId(foundImageID);

        if(userImageFile != null){
            Logger.debug("found an image in: " + getClass().getName() + ", image name is: " + userImageFile.fileName);
            setFileName(userImageFile.fileName);
            File existingFile = findUploadedFile(USERPATH + File.separator + user.getId(), userImageFile.fileName);
            setUploadedFile(existingFile);
        }
    }

    private static User checkForValidUser(String userId){

        User user = User.find.byId(userId);

        if(user == null){
            throw new NullPointerException("no user found with id: " + userId);
        }
        return user;
    }

    private void setUser(User user) {
        this.user = user;
    }

    // writes to exclusive file directory based upon userId
    public void uploadProfilePicture(){

        List<UserImageFile> userImageUploads = UserImageFile.findImagesByUser(getUser());

        for(UserImageFile userImageFile : userImageUploads){
            if(userImageFile.fileName.equals(getFileName())){
                setReturnMessage("Image with that name already exists in your folder");
                return;
            }
        }

        if(!maxCountReached()){
            String fullPath = writeFile(USERPATH + File.separator + getUser().getId(), getFileName());

            UserImageFile userImageFile = new UserImageFile(getUser(), fullPath, getFileName());
            Ebean.save(userImageFile);
            setReturnMessage("Successfully added profilepicture: " + userImageFile.fileName);
        }else{
            setReturnMessage("Maximum amount of images reached");
        }
    }

    // delets from equivalent userID
    public void deleteProfilePicture(){

        List<UserImageFile> userImageList = UserImageFile.findImagesByUser(getUser());

        for(UserImageFile userImageFile : userImageList){

            if(userImageFile.fileName.equals(getFileName())){
                deleteFile(USERPATH + File.separator + getUser().getId(), userImageFile.fileName);
                Ebean.delete(userImageFile);
                setReturnMessage("Successfully removed profilepicture: " + userImageFile.fileName);
                break;
            }
        }
    }

    public User getUser() {
        return user;
    }

    public boolean maxCountReached(){
        boolean maxCountReached = false;

        List<UserImageFile> userImageFiles = UserImageFile.findImagesByUser(getUser());

        if(userImageFiles.size() >= MAXUSERIMAGES){
            maxCountReached = true;
        }

        return maxCountReached;
    }

    public static ObjectNode fetchJson(String userID){
        User user = checkForValidUser(userID);
        ObjectNode outerObject = Json.newObject();

        if(user == null){
            throw new NullPointerException("no user associated with given ID");
        }

        List<UserImageFile> userImageFiles = UserImageFile.findImagesByUser(user);

        int count = 0;
        for(UserImageFile userImageFile : userImageFiles){

            ObjectNode innerObject = Json.newObject();
            innerObject.put("url", routes.Assets.at(USERPATH +
                    File.separator + user.getId() + File.separator + userImageFile.fileName).url());
            innerObject.put("id", userImageFile.id);
            innerObject.put("name", userImageFile.fileName);
            outerObject.put("file" + count++, innerObject);
        }

        return outerObject;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public static String getUseridfield() {
        return USERIDFIELD;
    }

}
