package utils;

import models.Event;

import java.util.Comparator;

public class EventSorter implements Comparator<Event> {
    @Override
    public int compare(Event e1, Event e2) {
        if(e1 == e2 || e1.getId().equals(e2.getId())) {
            return 0;
        } else return e1.getStartTime().compareTo(e2.getStartTime());
    }
}
