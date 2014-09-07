package powerups.core.feedpowerup;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import models.Club;
import models.Feed;
import models.PowerupModel;
import models.User;
import play.Logger;
import play.mvc.Result;
import play.mvc.Results;
import play.twirl.api.Html;
import powerups.Powerup;
import utils.Context;
import utils.FeedSorter;
import utils.imageuploading.WriteFiles;
import utils.imaging.ImageLinkValidator;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FeedPowerup extends Powerup implements WriteFiles{

    private static final String MESSAGE = "message";
    private static final String MESSAGE_TITLE = "messageTitle";
    private static final String PICTURE_URL = "pictureUrl";
    private static final String FEED_DIR_IDENTIFIER = "feed";
    private static final int MAX_FEED_INDEX_SIZE = 3;
    private List<Feed> userFeedList;
    private List<Feed> adminList;
    private User user = null;

    public FeedPowerup(Club club, PowerupModel model) {
        super(club, model);

        if (club != null && model != null) {

            /*
            * Find all feeds from club, sort them by date, and copy
            * the first 3 into new lists to be rendered by view.
            * */

            List<Feed> feedList = Feed.findByClub(club);
            adminList = new ArrayList<>();
            userFeedList = new ArrayList<>();
            user = Context.getContext(getClub()).getSender();

            if (user == null) {
                Logger.warn("user in FeedPowerup was null");
                return;
            }

            if (feedList != null) {
                feedList.sort(new FeedSorter());
                Collections.reverse(feedList);

                for (int i = 0; i < feedList.size(); i++) {

                    if (i < MAX_FEED_INDEX_SIZE) {
                        adminList.add(feedList.get(i));
                        userFeedList.add(feedList.get(i));
                    } else {
                        break;
                    }
                }

            } else {
                Logger.warn("feedList in FeedPowerup is null");
            }
        }
    }

    @Override
    public Html renderAdmin() {
        return powerups.core.feedpowerup.html.admin.render(adminList);
    }

    @Override
    public Html render() {
        return powerups.core.feedpowerup.html.powerup.render(userFeedList);
    }

    @Override
    public void activate() {

    }

    @Override
    public void deActivate() {

    }

    @Override
    public Result update(JsonNode updateContent) {

        if (updateContent != null && !updateContent.isNull()) {
            ImageLinkValidator validator = new ImageLinkValidator(new Dimension(500, 250), new Dimension(1000, 500), true);
            String message = updateContent.get(MESSAGE).asText();
            String messageTitle = updateContent.get(MESSAGE_TITLE).asText();
            String pictureUrl = updateContent.get(PICTURE_URL).asText();

            if (message == null || message.equals("") || messageTitle == null || messageTitle.equals("")
                    || pictureUrl == null || pictureUrl.equals("")) {
                Logger.info("shit is wrong");
                return Results.status(NO_UPDATE, "Vennligst fyll ut alle felt for nyheten");
            }

            ImageLinkValidator.StatusMessage statusMessage = validator.validate(pictureUrl);

            if (statusMessage.isSuccess()) {

                // todo implement imagewriting
                // in order to rewrite image to server
                String[] fileUrl = pictureUrl.split("/");
                String fileName = fileUrl[fileUrl.length - 1];

                Feed feed = new Feed(getClub(), user, messageTitle, message, pictureUrl);
                Ebean.save(feed);

                Feed wr = Feed.find.byId(feed.id);
                Logger.debug(wr.pictureUrl);

                return Results.ok("Feed-post opprettet");
            } else {
                return Results.status(NO_UPDATE, statusMessage.getMessage());
            }

        } else {
            return Results.status(NO_UPDATE, "Manglende info fra feltene");
        }
    }

    @Override
    public String getFileNameFromPath(String fileUrl) {
        return null;
    }

    @Override
    public String writeFile(String fileName, String subDir) {
        return null;
    }
}
