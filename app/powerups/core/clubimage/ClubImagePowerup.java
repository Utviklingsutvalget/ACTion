package powerups.core.clubimage;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import models.Club;
import models.PowerupModel;
import org.joda.time.LocalDateTime;
import play.mvc.Result;
import play.mvc.Results;
import play.twirl.api.Html;
import powerups.Powerup;
import powerups.core.clubimage.html.powerup;
import powerups.models.ClubImage;
import utils.MembershipLevel;
import utils.imageuploading.ImageUpload;
import utils.imageuploading.WriteFiles;
import utils.imaging.ImageLinkValidator;

import java.awt.*;
import java.io.File;

public class ClubImagePowerup extends Powerup implements WriteFiles{

    public static final String DEFAULT_IMAGE = "/assets/images/no_club_image.jpg";

    private static final String CLUB_IMAGE_IDENTIFIER = "clubimage";
    private final ClubImage clubImage;

    public ClubImagePowerup(Club club, PowerupModel model) {
        super(club, model);

        clubImage = ClubImage.find.byId(new ClubImage(this.getClub(), "").key);
    }

    @Override
    public Html render() {
        return powerup.render(this.clubImage.imageUrl, false);
    }

    @Override
    public Html renderAdmin() {
        return powerup.render(this.clubImage.imageUrl, true);
    }

    @Override
    public void activate() {
        ClubImage clubImage = new ClubImage(this.getClub(), DEFAULT_IMAGE);
        Ebean.save(clubImage);
    }

    @Override
    public void deActivate() {
        Ebean.delete(clubImage);
    }

    @Override
    public Result update(JsonNode updateContent) {
        if (!(this.getContext().getMemberLevel().getLevel() >= MembershipLevel.BOARD.getLevel())) {
            return Results.unauthorized();
        }
        String url = updateContent.get("link").asText();
        ImageLinkValidator validator = new ImageLinkValidator(new Dimension(800, 300), new Dimension(1600, 600));
        // ImageLinkValidator.StatusMessage statusMessage = validator.validate(url);

        String fileName = getFileNameFromPath(url);

        //ImageLinkValidator.StatusMessage statusMessage = validator.validate(pictureUrl);
        ImageLinkValidator.StatusMessage statusMessage = validator.validate(ImageUpload.getFileFromDefaultDir(
                fileName
        ));

        if (statusMessage.isSuccess()) {

            String newUrl = writeFile(fileName, getClub().id.toString() + File.separator + CLUB_IMAGE_IDENTIFIER +
                    File.separator + clubImage.key.toString() + new LocalDateTime());
            clubImage.imageUrl = newUrl;

            Ebean.update(clubImage);
            return Results.ok("Utvalgsbilde endret");
        } else {
            return Results.status(NO_UPDATE, statusMessage.getMessage());
        }
    }

    @Override
    public String getFileNameFromPath(String fileUrl) {
        String[] pictureUrl = fileUrl.split("/");
        return pictureUrl[pictureUrl.length - 1];
    }

    @Override
    public String writeFile(String fileName, String subDir) {
        File f = ImageUpload.getFileFromDefaultDir(fileName);

        ImageUpload imageUpload = new ImageUpload(f, f.getName());
        imageUpload.writeFile(subDir, imageUpload.getFileName());

        String newUrl = imageUpload.returnFileUrl();

        ImageUpload.clearDefaultDir();
        return newUrl;
    }
}
