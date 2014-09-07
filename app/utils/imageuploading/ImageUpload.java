package utils.imageuploading;

import play.Logger;
import play.api.Play;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import controllers.routes;

public class ImageUpload {

    private File uploadedFile;
    private String subdir;
    private String fileName;
    private static final String WRITE_IMAGE_ROOT_PATH = Play.current().path().getAbsolutePath() +
            File.separator + "public";
    private static final String DEFAULT_IMAGE_FOLDER = "defaultimagefolder";
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

        if(doubleCheckExtensions(fileName)){
            this.fileName = fileName;
        }else{
            throw new IllegalArgumentException("file has wrong format");
        }
    }

    public static void clearDefaultDir(){


    }

    public boolean doubleCheckExtensions(String fileName){
        String[] validExtensions = {".jpeg", ".jpg", ".png"};

        for(String extension : validExtensions){

            if(fileName.endsWith(extension)){

                return true;
            }
        }
        return false;
    }

    public String writeFileDefault(){
        return writeFile(DEFAULT_IMAGE_FOLDER, getFileName());
    }

    public int hashCode(String fileName) {
        int parentHash = super.hashCode();
        int fileHash = fileName.hashCode();

        fileHash *= (parentHash * 31);

        Logger.debug("hash: " + fileHash);
        return fileHash;
    }

    // writes file to designated subdirectory
    public String writeFile(String subDirectory, String fileName) {
        setSubdir(subDirectory);
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

    public static File getFileFromDefaultDir(String fileName){
        File file = Play.getFile(READ_IMAGE_ROOT_PATH + File.separator + DEFAULT_IMAGE_FOLDER +
                File.separator + fileName, Play.current());

        if(file.exists()){
            return file;
        }else{
            return null;
        }
    }

    public static String checkAndReturnFileUrl(String fileName){

        File file = Play.getFile(READ_IMAGE_ROOT_PATH + File.separator + DEFAULT_IMAGE_FOLDER +
                File.separator + fileName, Play.current());

        if(file.exists()){
            Logger.debug("found file: " + routes.Assets.at(DEFAULT_IMAGE_FOLDER +
                    File.separator + file.getName()).url());
            return routes.Assets.at(DEFAULT_IMAGE_FOLDER +
                    File.separator + file.getName()).url();
        }else{
            Logger.debug("no file found");
            return null;
        }
    }

    public String getSubdir() {
        return subdir;
    }

    public void setSubdir(String subdir) {

        this.subdir = subdir;
    }

    public String returnFileUrl(){
        File f = Play.getFile(READ_IMAGE_ROOT_PATH + File.separator + subdir + File.separator +
                getFileName(), Play.current());

        if(f.exists()){
            return routes.Assets.at(getSubdir() +
                    File.separator + f.getName()).url();
        }else{
            return null;
        }
    }

    public static File getUploadedFileDefaultDir(String fileName){
        List<File> fileList = new ArrayList<>();
        List<File> uncheckedDirs = new ArrayList<>();

        File rootDir = Play.getFile(READ_IMAGE_ROOT_PATH + File.separator + DEFAULT_IMAGE_FOLDER, Play.current());

        // check default rootdir
        if(rootDir.isDirectory()){

            File[] filesInDir = rootDir.listFiles();

            if(filesInDir == null){
                return null;
            }

            for(File file : filesInDir){

                if(file.isFile()){
                    fileList.add(file);

                }else if(file.isDirectory()){
                    uncheckedDirs.add(file);
                }
            }
        }

        // check all existing dirs
        fileList.addAll(traverseDirs(uncheckedDirs));

        // return search results
        for(File file : fileList){

            if(file.getName().equals(fileName)){
                Logger.debug("got em!");
                return file;
            }
        }

        return null;
    }

    private static List<File> traverseDirs(List<File> uncheckedDirs){
        List<File> fileList = new ArrayList<>();

        int count = 0;
        while(!uncheckedDirs.isEmpty()){

            File f = uncheckedDirs.get(count++);

            if(f.isDirectory()){

                File[] filesInDir = f.listFiles();

                if(filesInDir != null){

                    for(File file : filesInDir){

                        if(file.isDirectory()){
                            uncheckedDirs.add(file);
                        }else{
                            fileList.add(file);
                        }
                    }
                }
                uncheckedDirs.remove(f);

            }else{
                fileList.add(f);
            }
        }

        return fileList;
    }

    public File findUploadedFile(String subDir, String fileName){

        File file = Play.getFile(READ_IMAGE_ROOT_PATH + File.separator + subDir + File.separator + fileName, Play.current());

        if(file.exists()){
            return file;
        }

        throw new IllegalArgumentException("did not find file: " +
                READ_IMAGE_ROOT_PATH + File.separator + subDir + File.separator + fileName);
    }

    public void deleteFile(String subDirectory, String fileName){

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
