package utils;

import models.InitiationGroup;

import java.util.Comparator;

public class InitiationSorter implements Comparator<InitiationGroup> {
    @Override
    public int compare(InitiationGroup o1, InitiationGroup o2) {
        if (o1 == o2 || o1.getGroupNumber() == o2.getGroupNumber()) {
            return 0;
        }
        return o1.getGroupNumber() - o2.getGroupNumber();
    }

}
