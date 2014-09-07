package utils.imageuploading;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.routes;
import models.Club;
import models.ClubImageFile;
import models.Feed;
import models.FeedImageFile;
import play.libs.Json;

import java.io.File;
import java.util.Map;

public class FeedImageUpload extends ImageUpload {

    private static final String FEEDFIELD = "feedId";
    private static final String FEEDPATH = "feedImages";
    private Feed feed;
    private String returnMessage = "error";

    public FeedImageUpload(Map<String, String[]> map, File file, String fileName){
        super(file, fileName);

        setFeed(parseAndGetFeed(FieldFetch.getFieldValue(FEEDFIELD, map)));
    }

    public FeedImageUpload(String feedIdString){
        setFeed(parseAndGetFeed(feedIdString));

        FeedImageFile feedImageFile = FeedImageFile.findPathByFeed(getFeed());

        if(feedImageFile != null){
            File existingFile = findUploadedFile(FEEDPATH + File.separator +
                    feedImageFile.feed.club.id + File.separator + feedImageFile.feed.id, feedImageFile.fileName);
            setFileName(feedImageFile.fileName);
            setUploadedFile(existingFile);
        }
    }

    public void deleteFeedImage(){
        FeedImageFile feedImageFile = FeedImageFile.findPathByFeed(getFeed());

        if(feedImageFile != null){
            deleteFile(FEEDPATH + File.separator +
                    feedImageFile.feed.club.id + File.separator + feedImageFile.feed.id, feedImageFile.fileName);
            Ebean.delete(feedImageFile);
            setReturnMessage("Successfully deleted feedimage: " + feedImageFile.fileName);
        }
    }

    public static Feed parseAndGetFeed(String feedId){

        Long foundFeedId = Long.parseLong(feedId);

        Feed feed = Feed.find.byId(foundFeedId);

        if(feed == null){
            throw new IllegalArgumentException("No feed with that ID found");
        }
        return feed;
    }

    public void uploadFeedImage(){

        FeedImageFile feedImageFile = FeedImageFile.findPathByFeed(getFeed());

        if(feedImageFile == null){

            String fullPath = writeFile(FEEDPATH + File.separator + getFeed().club.id + File.separator +
                    getFeed().id, getFileName());
            FeedImageFile feedImageFile1 = new FeedImageFile(getFeed(), fullPath, getFileName());
            Ebean.save(feedImageFile1);
            setReturnMessage("FeedImageFile saved: " + feedImageFile1.fileName);
        }
    }

    public static ObjectNode fetchJson(String feedIDString){
        Feed feed = parseAndGetFeed(feedIDString);
        ObjectNode outerObject = Json.newObject();
        ObjectNode innerObject = Json.newObject();

        if(feed == null){
            throw new NullPointerException("no feed associated with given ID");
        }

        FeedImageFile feedImageFile = FeedImageFile.findPathByFeed(feed);

        if(feedImageFile != null){
            innerObject.put("url", routes.Assets.at(FEEDPATH + File.separator + feedImageFile.feed.club.id + File.separator +
                    feedImageFile.feed.id + File.separator + feedImageFile.fileName).url());
            innerObject.put("id", feedImageFile.id);
            innerObject.put("name", feedImageFile.fileName);

            outerObject.put("file", innerObject);
        }

        return outerObject;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
    }

    public Feed getFeed() {
        return feed;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }
}
