package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import models.*;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Authorize;
import utils.FeedSorter;
import utils.MembershipLevel;

import java.util.*;


public class Feeds extends Controller {

    private static final String TITLE = "Feed";
    private static final String CONTENT = "content";

    /*
    * has yet to be implemented in routes file
    * */

    public static Result index(){

        List<Feed> feedList = new ArrayList<>();

        try{
            User user = new Authorize.UserSession().getUser();
            List<Club> clubList = Club.find.all();

            for(Club club : clubList){

                Membership membership = Membership.find.byId(new Membership(club, user).id);

                if(membership != null){
                    if(membership.level.getLevel() >= MembershipLevel.SUBSCRIBE.getLevel()){

                        List<Feed> clubFeedList = Feed.findByClub(club);

                        for(Feed feed : clubFeedList){

                            if(feed.dateTime.isAfterNow()){
                                feedList.add(feed);
                            }
                        }
                    }
                }
            }

        }catch(Authorize.SessionException e){

            return unauthorized("You need to be logged in to see feed");
        }

        if(feedList != null && !feedList.isEmpty()){
            feedList.sort(new FeedSorter());
        }

        return ok(views.html.feed.index.render(feedList, TITLE));
    }

    public static Result create(Long clubId){

        Map<String, String> titleAndMessage = new HashMap<>();
        Club club = Club.find.byId(clubId);

        JsonNode json = request().body().asJson();

        // TODO FIX JSON PARSING TO THIS METHOD

        try{
            User user = new Authorize.UserSession().getUser();
            if(json != null){

                Iterator<String> fields = json.fieldNames();

                while(fields.hasNext()){

                    String title = fields.next();

                    if(title.equals(TITLE)){
                        titleAndMessage.put(title, json.get(title).asText());
                    }
                }

                for(String title : titleAndMessage.keySet()){

                    // TODO NEEDS IMPLEMENTATION OF CLUBID FOR FEEDENTRY
                    Feed feedEntry = new Feed(club, titleAndMessage.get(title), title);
                    Ebean.save(feedEntry);
                }
            }

        }catch(Authorize.SessionException e){
            e.printStackTrace();
            return unauthorized();
        }

        return ok();
    }
}
