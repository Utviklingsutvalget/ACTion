package powerups.core.feedpowerup;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import models.*;
import play.Logger;
import play.mvc.Result;
import play.mvc.Results;
import play.twirl.api.Html;
import powerups.Powerup;
import utils.Context;
import utils.FeedSorter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FeedPowerup extends Powerup {

    private List<Feed> feedList;
    private List<Feed> userFeedList;
    private List<Feed> adminList;
    private static final String MESSAGE = "message";
    private static final String MESSAGETITLE = "messageTitle";
    private static final int MAXFEEDSIZE = 3;
    private User user = null;

    public FeedPowerup(Club club, PowerupModel model){
        super(club, model);

        if(club != null && model != null){

            /*
            * Find all feeds from club, sort them by date, and copy
            * the first 3 into new lists to be rendered by view.
            * */

            feedList = Feed.findByClub(club);
            adminList = new ArrayList<>();
            userFeedList = new ArrayList<>();
            user = Context.getContext(getClub()).getSender();

            if(user == null){
                Logger.warn("user in FeedPowerup was null");
                return;
            }

            if(feedList != null){
                feedList.sort(new FeedSorter());
                Collections.reverse(feedList);

                for(int i = 0; i < feedList.size(); i++){

                    if(i < MAXFEEDSIZE){
                        adminList.add(feedList.get(i));
                        userFeedList.add(feedList.get(i));
                    }else{
                        break;
                    }
                }
            }else{
                Logger.warn("feedList in FeedPowerup is null");
            }
        }
    }

    @Override
    public Html renderAdmin(){
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

        if(updateContent != null && !updateContent.isNull()){

            String message = updateContent.get(MESSAGE).asText();
            String messageTitle = updateContent.get(MESSAGETITLE).asText();

            if(message.equalsIgnoreCase("") || messageTitle.equalsIgnoreCase("")){
                return Results.badRequest("Vennligst fyll ut både tittel og innhold for FeedPost");
            }

            Logger.info("jsonNode in Feed found the following: " +
                    "message : " + message + ", \n" +
                    "messagetitle : " + messageTitle + ", \n" +
                    "clubname : " + getClub().name + ", \n" +
                    "user firstName : " + user.firstName + ".");

            Feed feed = new Feed(getClub(), user, messageTitle, message);
            Ebean.save(feed);

        }else{

            Logger.info("Json in update for FeedPowerup was null");
        }

        return Results.ok();
    }
}
