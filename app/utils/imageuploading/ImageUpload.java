package utils.imageuploading;

import com.avaje.ebean.Ebean;
import models.ProfileImageFile;
import models.User;
import play.Logger;
import play.api.Play;
import java.io.*;
import java.util.Map;

public class ImageUpload {

    private static final String FEEDFIELD = "feedId";

    private Map<String, String[]> paramsMap;
    private File uploadedFile;
    private String fileName;
    private static final String WRITE_IMAGE_ROOT_PATH = Play.current().path().getAbsolutePath() +
            File.separator + "public" + File.separator;
    private static final String READ_IMAGE_ROOT_PATH = "public" + File.separator;
    private static final String FEEDPATH = "feeds";


    /**
     *
     * All images can now be reached via controller.Assets.at("directory/filename.extension")
     * ex:
     * <img src="@routes.Assets.at("clubimages/screwedupprofile.jpg")">
     *
     * implement hash function for images somehow.
     * */


    public ImageUpload(Map<String, String[]> inputMap, File file, String fileName) {
        this.paramsMap = inputMap;
        this.uploadedFile = file;
        setFileName(fileName);

        // resolve intent and redirect
    }

    private void setFileName(String fileName){

        if(doubleCheckExtensions(fileName)){
            this.fileName = fileName;
        }else{
            throw new IllegalArgumentException("file has wrong format");
        }
    }

    public static String getFieldValue(String key, Map<String, String[]> paramsMap){
        String[] paramArray = paramsMap.get(key);
        String val = paramArray[0];

        Logger.debug("key: " + key + ", val: " + val);

        return val;
    }

    protected String getFieldValue(String key){
        String[] paramArray = paramsMap.get(key);
        String val = paramArray[0];

        Logger.debug("key: " + key + ", val: " + val);

        return val;
    }

    protected boolean doubleCheckExtensions(String fileName){
        String[] validExtensions = {".jpeg", ".jpg", ".png"};

        for(String extension : validExtensions){

            if(fileName.endsWith(extension)){

                return true;
            }
        }
        return false;
    }

    // writes file to designated subdirectory
    protected String writeFile(String subDirectory, String fileName) {

        File dir = new File(WRITE_IMAGE_ROOT_PATH
                + File.separator + subDirectory + File.separator);
        dir.mkdir();
        File picture = new File(dir, fileName);

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

    private File findUploadedFile(String subDir, String fileName){

        File file = Play.getFile(READ_IMAGE_ROOT_PATH + subDir + File.separator + fileName, Play.current());

        if(file.exists()){
            return file;
        }

        throw new IllegalArgumentException("did not find file: " +
                READ_IMAGE_ROOT_PATH + subDir + File.separator + fileName);
    }

    protected void deleteFile(String subDirectory, String fileName){

        File file = findUploadedFile(subDirectory, fileName);

        if(file.delete()){
            Logger.debug(file.getName() + " deleted");
        }else{
            Logger.debug("could not delete file");
        }
    }


}
