package utils.imageuploading;

import com.avaje.ebean.Ebean;
import controllers.Application;
import models.ProfileImageFile;
import models.User;
import play.Logger;
import play.api.Play;
import java.io.*;
import java.net.URI;
import java.util.Map;

public class ImageUpload {

    private File uploadedFile;
    private String fileName;
    private static final String WRITE_IMAGE_ROOT_PATH = Play.current().path().getAbsolutePath() +
            File.separator + "public";
    private static final String READ_IMAGE_ROOT_PATH = "public";

    /**
     *
     * All images can now be reached via controller.Assets.at("directory/filename.extension")
     * ex:
     * <img src="@routes.Assets.at("clubimages/screwedupprofile.jpg")">
     *
     * implement hash function for images somehow.
     * */

    public ImageUpload(File file, String fileName) {
        setUploadedFile(file);
        setFileName(fileName);
    }

    public ImageUpload(){
        setUploadedFile(null);
        this.fileName = "";
    }

    public void setUploadedFile(File uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public void setFileName(String fileName){

        Logger.debug(fileName);

        if(doubleCheckExtensions(fileName)){
            this.fileName = fileName;
        }else{
            throw new IllegalArgumentException("file has wrong format");
        }
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

        File dir = new File(WRITE_IMAGE_ROOT_PATH + File.separator + subDirectory + File.separator);

        if(!dir.exists() && !dir.isDirectory()){
            dir.mkdirs();
        }

        File picture = new File(dir, fileName);

        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;

        try {

            fileInputStream = new FileInputStream(uploadedFile);
            fileOutputStream = new FileOutputStream(picture);

            int content;

            while ((content = fileInputStream.read()) != -1) {

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
        return subDirectory + File.separator + picture.getName();
    }

    public String getFileName() {
        return fileName;
    }

    public File findUploadedFile(String subDir, String fileName){

        File file = Play.getFile(READ_IMAGE_ROOT_PATH + File.separator + subDir + File.separator + fileName, Play.current());

        if(file.exists()){
            return file;
        }

        throw new IllegalArgumentException("did not find file: " +
                READ_IMAGE_ROOT_PATH + File.separator + subDir + File.separator + fileName);
    }

    protected void deleteFile(String subDirectory, String fileName){

        File file = findUploadedFile(subDirectory, fileName);

        if(file.delete()){
            Logger.debug(file.getName() + " deleted");
        }else{
            Logger.debug("could not delete file");
        }
    }

    public static String getReadImageRootPath() {
        return READ_IMAGE_ROOT_PATH;
    }
}
