package utils;

import models.Event;

import java.util.Comparator;

public class EventSorter implements Comparator<Event> {
    @Override
    public int compare(Event e1, Event e2) {
        if(e1 == e2 || e1.id.equals(e2.id)) {
            return 0;
        } else return e1.startTime.compareTo(e2.startTime);
    }
}
