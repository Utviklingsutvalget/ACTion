package utils.imageuploading;

import play.Logger;

import java.io.File;
import java.util.Map;

public class UploadHandler {

    private static final String PROFILE = "profilePicture";
    private static final String CLUB = "clubPicture";
    private static final String FEED = "feedPicture";
    private static final String INTENTFIELD = "intent";
    private ImageUpload imageUpload;
    private Map<String, String[]> paramsMap;
    private File file;
    private String fileName;
    private String returnMessage = "error";

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {

        this.returnMessage = returnMessage;
    }

    public UploadHandler(Map<String, String[]> inputMap, File file, String fileName) {

        if(inputMap == null){
            throw new NullPointerException("paramsMap in Imageuploading is null");
        }

        if(file == null){
            throw new NullPointerException("file object is empty");
        }

        setParamsMap(inputMap);
        setFile(file);
        setFileName(fileName);

        String intent = ImageUpload.getFieldValue(INTENTFIELD, inputMap);
        Logger.debug("intent: " + intent);
        intentResolve(intent);
    }

    private void intentResolve(String intent) {

        switch (intent){
            case PROFILE : setImageUpload(new UserImageUpload(getParamsMap(), getFile(), getFileName()));
                           profileHandler();
                break;
            case CLUB : setImageUpload(new ClubImageUpload(getParamsMap(), getFile(), getFileName()));
                        clubHandler();
                break;
            case FEED : //method
                break;
            default: throw new IllegalArgumentException("intent matches none of identified types");
        }
    }

    private void profileHandler(){
        UserImageUpload userImageUpload = (UserImageUpload) getImageUpload();
        setReturnMessage(userImageUpload.getReturnMessage());
    }

    private void clubHandler(){
        ClubImageUpload clubImageUpload = (ClubImageUpload) getImageUpload();
        clubImageUpload.uploadClubPicture();

        setReturnMessage(clubImageUpload.getReturnMessage());
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
}
