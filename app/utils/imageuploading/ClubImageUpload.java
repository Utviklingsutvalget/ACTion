package utils.imageuploading;

import com.avaje.ebean.Ebean;
import models.Club;
import models.ClubImageFile;

import java.io.File;
import java.util.Map;

public class ClubImageUpload extends ImageUpload {

    private static final String CLUBFIELD = "clubId";
    private static final String CLUBPATH = "clubimages";
    private Club club;
    private String returnMessage = "error";

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public ClubImageUpload(Map<String, String[]> inputMap, File file, String fileName) {
        super(inputMap, file, fileName);

        setClub(parseAndGetClub(getFieldValue(CLUBFIELD)));
    }

    private Club parseAndGetClub(String clubIdString){

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

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }
}
