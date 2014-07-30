package controllers;

import models.*;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Authorize;
import utils.FeedSorter;
import utils.MembershipLevel;

import java.util.*;


public class Feeds extends Controller {

    //private static final int MAXIMUMFEEDSIZE = 30;
    private static final int MAXFEEDSPERCLUB = 5;

    /*
    * has yet to be implemented in routes file
    * */

    public static Result index(){

        List<Feed> feedList = new ArrayList<>();

        // TODO FIND A MORE EFFECTIVE IMPLEMENTATION LATER

        //Check all clubs for membership with given user. extract n amount
        //of feeds from each club and return list to render.
        try{
            User user = new Authorize.UserSession().getUser();
            List<Club> clubList = Club.find.all();

            for(Club club : clubList){

                Membership membership = Membership.find.byId(new Membership(club, user).id);

                if(membership != null){

                    if(membership.level.getLevel() >= MembershipLevel.SUBSCRIBE.getLevel()){

                        feedList.addAll(getClubFeed(club));
                    }
                }
            }

        }catch(Authorize.SessionException e){

            return unauthorized("You need to be logged in to see feed");
        }

        if(feedList != null){
            feedList.sort(new FeedSorter());
            Collections.reverse(feedList);
        }

        return ok(views.html.feed.index.render(feedList));
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