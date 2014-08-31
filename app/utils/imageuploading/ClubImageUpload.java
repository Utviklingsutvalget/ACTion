package utils.imageuploading;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.routes;
import models.Club;
import models.ClubImageFile;
import models.User;
import models.UserImageFile;
import play.Logger;
import play.libs.Json;

import java.io.File;
import java.util.List;
import java.util.Map;

public class ClubImageUpload extends ImageUpload {

    private static final String CLUBFIELD = "clubId";
    private static final String CLUBPATH = "clubimages";
    private Club club;
    private String returnMessage = "error";

    public ClubImageUpload(Map<String, String[]> inputMap, File file, String fileName) {
        super(file, fileName);

        setClub(parseAndGetClub(FieldFetch.getFieldValue(CLUBFIELD, inputMap)));
    }

    public ClubImageUpload(String clubIDString) {
        setClub(parseAndGetClub(clubIDString));
        ClubImageFile clubImageFile = ClubImageFile.findPathByClub(getClub());

        if(clubImageFile != null){
            File existingFile = findUploadedFile(CLUBPATH + File.separator + clubImageFile.club.id, clubImageFile.fileName);
            setFileName(clubImageFile.fileName);
            setUploadedFile(existingFile);
        }
    }

    private static Club parseAndGetClub(String clubIdString){

        Long clubId = Long.parseLong(clubIdString);

        Club club = Club.find.byId(clubId);
        if(club == null){
            throw new IllegalArgumentException("club id passed is invalid");
        }

        return club;
    }

    public void uploadClubPicture() {

        ClubImageFile clubImageFile = ClubImageFile.findPathByClub(getClub());

        if(clubImageFile == null){
            // write file to dir on server
            String fullPath = writeFile(CLUBPATH + File.separator + getClub().id, getFileName());

            // save path and clubId in DB
            ClubImageFile clubImageFile1 = new ClubImageFile(getClub(), fullPath, getFileName());
            Ebean.save(clubImageFile1);
            setReturnMessage("Successfully saved clubImage: " + clubImageFile1.fileName);

        }else{

            deleteFile(CLUBPATH + File.separator + getClub().id, clubImageFile.fileName);
            clubImageFile.path = writeFile(CLUBPATH, getFileName());
            clubImageFile.fileName = getFileName();
            Ebean.save(clubImageFile);
            setReturnMessage("Successfully changed clubImage to: " + clubImageFile.fileName);
        }
    }

    public void deleteClubPicture(){
        ClubImageFile clubImageFile = ClubImageFile.findPathByClub(getClub());

        if(clubImageFile != null){
            deleteFile(CLUBPATH + File.separator + getClub().id, clubImageFile.fileName);
            clubImageFile.fileName = getFileName();
            Ebean.delete(clubImageFile);
            setReturnMessage("Successfully deleted clubImage: " + clubImageFile.fileName);
        }
    }

    public static ObjectNode fetchJson(String clubIDString){
        Club club = parseAndGetClub(clubIDString);
        ObjectNode outerObject = Json.newObject();
        ObjectNode innerObject = Json.newObject();

        if(club == null){
            throw new NullPointerException("no club associated with given ID");
        }

        ClubImageFile clubImageFile = ClubImageFile.findPathByClub(club);

        if(clubImageFile != null){
            innerObject.put("url", routes.Assets.at(CLUBPATH + File.separator +
                    clubImageFile.club.id + File.separator + clubImageFile.fileName).url());
            innerObject.put("id", clubImageFile.id);
            innerObject.put("name", clubImageFile.fileName);

            outerObject.put("file", innerObject);
        }

        return outerObject;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }
}
