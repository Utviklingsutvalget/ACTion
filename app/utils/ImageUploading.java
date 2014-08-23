package utils;

import com.avaje.ebean.Ebean;
import models.Club;
import models.ClubImageFile;
import models.ProfileImageFile;
import models.User;
import play.Logger;
import play.api.Play;
import java.io.*;
import java.util.Map;

public class ImageUploading {

    public static final String PROFILE = "profilePicture";
    public static final String CLUB = "clubPicture";
    public static final String FEED = "feedPicture";

    private static final String CLUBFIELD = "clubId";
    private static final String USERIDFIELD = "userId";
    private static final String INTENTFIELD = "intent";
    private static final String TASKFIELD = "task";
    private static final String TASK_DELETE = "delete";
    private static final String TASK_UPLOAD = "upload";
    private String task;
    private static final String FEEDFIELD = "feedId";

    private Map<String, String[]> paramsMap;
    private File uploadedFile;
    private String fileName;
    private File savedFile;
    private static final String WRITE_IMAGE_ROOT_PATH = Play.current().path().getAbsolutePath() +
            File.separator + "public" + File.separator;
    private static final String READ_IMAGE_ROOT_PATH = "public" + File.separator;
    private static final String CLUBPATH = "clubimages";
    private static final String USERPATH = "users";
    private static final String FEEDPATH = "feeds";


    /**
     *
     * IMPLEMENT FILEDELETING FUNCTION
     *
     * All images can now be reached via controller.Assets.at("directory/filename.extension")
     * ex:
     * <img src="@routes.Assets.at("clubimages/screwedupprofile.jpg")">
     *
     * implement hash function for images somehow.
     * */


    public ImageUploading(Map<String, String[]> inputMap, File file, String fileName) {
        this.paramsMap = inputMap;
        this.uploadedFile = file;
        setFileName(fileName);

        if(paramsMap == null){
            throw new NullPointerException("paramsMap in Imageuploading is null");
        }

        if(uploadedFile == null){
            throw new NullPointerException("file object is empty");
        }

        String task = getFieldValue(TASKFIELD);
        String intent = getFieldValue(INTENTFIELD);
        Logger.debug("intent: " + intent + ", task: " + task);

        // resolve intent and redirect
        intentResolve(intent);
    }

    private void setFileName(String fileName){

        if(doubleCheckExtensions(fileName)){
            this.fileName = fileName;
        }else{
            throw new IllegalArgumentException("file has wrong format");
        }
    }

    private void taskResolve(String task){

        switch (task){
            case TASK_UPLOAD : this.task = TASK_UPLOAD;
                break;
            case TASK_DELETE : this.task = TASK_DELETE;
                break;
            default: throw new IllegalArgumentException("task not recognised");
        }
    }

    private void intentResolve(String intent) {

        switch (intent){
            case PROFILE : profilePicture();
                break;
            case CLUB : clubPicture();
                break;
            case FEED : //method
                break;
            default: throw new IllegalArgumentException("intent matches none of identified types");
        }
    }

    private String getFieldValue(String key){
        String[] paramArray = paramsMap.get(key);
        String val = paramArray[0];

        Logger.debug("key: " + key + ", val: " + val);

        return val;
    }

    private boolean doubleCheckExtensions(String fileName){
        String[] validExtensions = {".jpeg", ".jpg", ".png"};

        for(String extension : validExtensions){

            if(fileName.endsWith(extension)){

                return true;
            }
        }
        return false;
    }

    // writes file to designated subdirectory
    private String writeFile(String subDirectory, String fileName) {

        File dir = new File(WRITE_IMAGE_ROOT_PATH
                + File.separator + subDirectory + File.separator);
        dir.mkdir();
        File picture = new File(dir, fileName);
        savedFile = picture;

        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;

        try{

            fileInputStream = new FileInputStream(uploadedFile);
            fileOutputStream = new FileOutputStream(picture);
            int content;

            while((content = fileInputStream.read()) != -1){

                fileOutputStream.write(content);
            }

        }catch (IOException e){
            Logger.warn("error opening/writing to filestream in ImageUploading");

        }finally{

            try{

                fileInputStream.close();
                fileOutputStream.close();

            }catch (IOException e){
                Logger.warn("Error closing filestreams");
            }

        }
        Logger.debug("picture.getpath(): " + picture.getPath());

        return subDirectory + File.separator + picture.getName();
    }

    public String getFileName() {
        return fileName;
    }

    private Club parseAndGetClub(String clubIdString){

        Long clubId = Long.parseLong(clubIdString);

        Club club = Club.find.byId(clubId);
        if(club == null){
            throw new IllegalArgumentException("club id passed is invalid");
        }

        return club;
    }

    private void clubPicture() {

        Club club = parseAndGetClub(getFieldValue(CLUBFIELD));

        ClubImageFile clubImageFile = ClubImageFile.findPathByClub(club);

        if(clubImageFile == null){
            // write file to dir on server
            String fullPath = writeFile(CLUBPATH, fileName);

            // save path and clubId in DB
            ClubImageFile clubImageFile1 = new ClubImageFile(club, fullPath, fileName);
            Ebean.save(clubImageFile1);

        }else{

            deleteFile(CLUBPATH, clubImageFile.fileName);
            clubImageFile.path = writeFile(CLUBPATH, fileName);
            clubImageFile.fileName = fileName;
            Ebean.save(clubImageFile);
        }
    }

    private File getUploadedFile(String subDir, String fileName){

        File file = Play.getFile(READ_IMAGE_ROOT_PATH + subDir + File.separator + fileName, Play.current());

        if(file.exists()){
            return file;
        }

        throw new IllegalArgumentException("did not find file: " +
                READ_IMAGE_ROOT_PATH + subDir + File.separator + fileName);
    }

    private void deleteFile(String subDirectory, String fileName){

        File file = getUploadedFile(subDirectory, fileName);

        if(file.delete()){
            Logger.debug(file.getName() + " deleted");
        }else{
            Logger.debug("could not delete file");
        }
    }

    private void profilePicture(){

        String fullPath = writeFile(USERPATH, fileName);
        String userIDString = getFieldValue(USERIDFIELD);

        User user = User.find.byId(userIDString);
        if(user == null){
            throw new IllegalArgumentException("Did not find user associated with that id");
        }

        ProfileImageFile profileImageFile = new ProfileImageFile(user, fullPath);
        Ebean.save(profileImageFile);
    }
}
