package controllers;

import models.*;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Authorize;
import utils.FeedSorter;
import utils.MembershipLevel;

import java.util.*;


public class Feeds extends Controller {

    //private static final int MAXIMUMFEEDSIZE = 30;
    private static final int MAXFEEDSPERCLUB = 5;
    private static final int MAXINDEXFEEDSIZE = 14;

    /*
    * has yet to be implemented in routes file
    * */

    public static Result index(){

        List<Feed> feedList = new ArrayList<>();
        List<Feed> initialList = new ArrayList<>();
        List<Feed> remainingList = new ArrayList<>();

        // TODO FIND A MORE EFFECTIVE IMPLEMENTATION LATER

        //Check all clubs for membership with given user. extract n amount
        //of feeds from each club and return list to render.
        try{
            User user = new Authorize.UserSession().getUser();
            List<Club> clubList = Club.find.all();

            // Keeping this in as bugs with empty user tables have caused
            // Authorize check to go through but return a null object for user.
            if(user == null){
                Logger.warn("The user table is empty, yet passes Authorization check, " +
                        "returning internal server error. Check database, user table");
                return internalServerError("Noe gikk galt, kontakt administrator");
            }

            for(Club club : clubList){

                Membership membership = Membership.find.byId(new Membership(club, user).id);

                if(membership != null){

                    if(membership.level.getLevel() >= MembershipLevel.SUBSCRIBE.getLevel()){

                        feedList.addAll(getClubFeed(club));
                    }
                }
            }

        }catch(Authorize.SessionException e){

            return unauthorized(views.html.index.render("You need to be logged in to see feed"));
        }

        if(!feedList.isEmpty()){
            feedList.sort(new FeedSorter());
            Collections.reverse(feedList);
            Logger.info(feedList.size() + " is feedList size");

            //Adds correct entries to correct lists, up to 10 entries
            for(int i = 0; i < feedList.size(); i++){

                if(i < 2){

                    initialList.add(feedList.get(i));
                    Logger.info("added to initial " + i);

                }else if(i < MAXINDEXFEEDSIZE && i >= 2){

                    remainingList.add(feedList.get(i));
                    Logger.info("added to remaining " + i);

                }else{
                    Logger.info("Max of 14 has been reached or list has no more entries, terminating");
                    break;
                }
            }
        }

        return ok(views.html.feed.index.render(remainingList, initialList));
    }

    //Find up x feeds by a give club (until maxfeedsperclub is reached)
    //add to list, sort and return list
    public static List<Feed> getClubFeed(Club club){

        List<Feed> clubFeedList = Feed.findByClub(club);
        clubFeedList.sort(new FeedSorter());
        Collections.reverse(clubFeedList);

        List<Feed> feedList = new ArrayList<>();

        for(int i = 0; i < clubFeedList.size(); i++){

            if(i < MAXFEEDSPERCLUB){
                feedList.add(clubFeedList.get(i));
            }
        }

        return feedList;
    }
}
