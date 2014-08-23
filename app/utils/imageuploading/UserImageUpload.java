package utils.imageuploading;

import com.avaje.ebean.Ebean;
import models.User;
import models.UserImageFile;
import java.io.File;
import java.util.List;
import java.util.Map;

public class UserImageUpload extends ImageUpload {

    private static final String USERPATH = "users";
    private static final String TASK_DELETE = "delete";
    private static final String TASK_UPLOAD = "upload";
    private static final String TASKFIELD = "task";
    private static final String USERIDFIELD = "userId";
    private static final int MAXUSERIMAGES = 5;
    private String task;
    private User user;
    private String returnMessage = "error";

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public UserImageUpload(Map<String, String[]> inputMap, File file, String fileName) {
        super(inputMap, file, fileName);

        User user = checkForValidUser(getFieldValue(USERIDFIELD));
        setUser(user);

        setTask(getFieldValue(TASKFIELD));
        taskResolve(getTask());

    }

    public User checkForValidUser(String userId){

        User user = User.find.byId(userId);

        if(user == null){
            throw new NullPointerException("no user found with id: " + userId);
        }
        return user;
    }

    public void setUser(User user) {
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

    private void taskResolve(String task){

        switch (task){
            case TASK_UPLOAD : uploadProfilePicture();
                break;
            case TASK_DELETE : deleteProfilePicture();
                break;
            default: throw new IllegalArgumentException("task not recognised");
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

    public String getTask() {
        return task;
    }

    public void setTask(String task) {

        if(task == null || task.equals("")){
            throw new IllegalArgumentException("no task specified for profileImage");
        }
        this.task = task;
    }

    public static String getUseridfield() {
        return USERIDFIELD;
    }

    public static String getTaskfield() {
        return TASKFIELD;
    }

    public static String getTaskUpload() {
        return TASK_UPLOAD;
    }

    public static String getTaskDelete() {
        return TASK_DELETE;
    }
}
