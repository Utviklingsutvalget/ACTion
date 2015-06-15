package controllers;

import com.google.inject.Inject;
import models.Club;
import models.Feed;
import models.Membership;
import models.User;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.twirl.api.Content;
import services.ClubService;
import services.FeedService;
import services.MembershipService;
import services.UserService;
import utils.FeedSorter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Feeds extends Controller {

    private static final int MAXFEEDSPERCLUB = 5;
    private static final int MAXINDEXFEEDSIZE = 14;

    @Inject
    private ClubService clubService;
    @Inject
    private FeedService feedService;
    @Inject
    private MembershipService membershipService;
    @Inject
    private UserService userService;

    public Result index() {

        List<Feed> feedList = new ArrayList<>();
        List<Feed> initialList = new ArrayList<>();
        List<Feed> remainingList = new ArrayList<>();

        // TODO FIND A MORE EFFECTIVE IMPLEMENTATION LATER

        //Check all clubs for membership with given user. extract n amount
        //of feeds from each club and return list to render.
        User user = userService.getCurrentUser(session());
        if(user != null) {
            List<Club> clubList = clubService.findAll();

            for (Club club : clubList) {

                Membership membership = membershipService.findById(new Membership(club, user).getId());

                //if (membership != null) {

                    //if (membership.level.getLevel() >= MembershipLevel.SUBSCRIBE.getLevel()) {

                        feedList.addAll(getClubFeed(club));
                    //}
                //}
            }

        } else {
            System.out.println("Ooopsie");

            List<Feed> defaultInitial = new ArrayList<>();
            List<Feed> defaultRemaining = new ArrayList<>();

            setupDefaultLists(defaultInitial, defaultRemaining);

            return ok((Content) views.html.feed.index.render(defaultRemaining, defaultInitial));
        }

        //setupUserLists(feedList, initialList, remainingList);
        setupDefaultLists(initialList, remainingList);
        return ok((Content) views.html.feed.index.render(remainingList, initialList));
    }

    // Pretty much does the same thing as setupuserLists except fetching all feeds.
    public void setupDefaultLists(List<Feed> defaultInitial, List<Feed> defaultRemaining) {

        // TODO FIND A MORE EFFICIENT WAY OF FINDING FEEDS AND SORTING
        List<Feed> allFeeds = feedService.findAll();

        if (!allFeeds.isEmpty()) {
            allFeeds.sort(new FeedSorter());
            Collections.reverse(allFeeds);
        }

        for (int i = 0; i < allFeeds.size(); i++) {

            if (i < 2) {

                defaultInitial.add(allFeeds.get(i));

            } else if (i >= 2 && i < MAXINDEXFEEDSIZE) {

                defaultRemaining.add(allFeeds.get(i));

            } else {
                break;
            }
        }
    }

    // sorts feedlist, then fetches first 14 entries (provided there are that many),
    // places first 2 in initialFeedList for top display, then inserts remaining 12 in remainingList.
    public void setupUserLists(List<Feed> feedList, List<Feed> initialList, List<Feed> remainingList) {

        if (!feedList.isEmpty()) {
            feedList.sort(new FeedSorter());
            Collections.reverse(feedList);
            Logger.info(feedList.size() + " is feedList size");

            //Adds correct entries to correct lists, up to 10 entries
            for (int i = 0; i < feedList.size(); i++) {

                if (i < 2) {

                    initialList.add(feedList.get(i));
                    Logger.info("added to initial " + i);

                } else if (i < MAXINDEXFEEDSIZE && i >= 2) {

                    remainingList.add(feedList.get(i));
                    Logger.info("added to remaining " + i);

                } else {
                    Logger.info("Max of 14 has been reached or list has no more entries, terminating");
                    break;
                }
            }
        }
    }

    //Find up x feeds by a give club (until maxfeedsperclub is reached)
    //add to list, sort and return list
    public List<Feed> getClubFeed(Club club) {

        List<Feed> clubFeedList = feedService.findByClub(club);
        clubFeedList.sort(new FeedSorter());
        Collections.reverse(clubFeedList);

        List<Feed> feedList = new ArrayList<>();

        for (int i = 0; i < clubFeedList.size(); i++) {

            if (i < MAXFEEDSPERCLUB) {
                feedList.add(clubFeedList.get(i));
            }
        }

        return feedList;
    }
}
