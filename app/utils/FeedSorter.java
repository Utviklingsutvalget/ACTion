package utils;

import models.Feed;

import java.util.Comparator;

public class FeedSorter implements Comparator<Feed> {

    @Override
    public int compare(Feed o1, Feed o2) {

        if(o1 == o2 || o1.getId().equals(o2.getId())){
            return 0;
        }

        return o1.getDateTime().compareTo(o2.getDateTime());
    }
}
