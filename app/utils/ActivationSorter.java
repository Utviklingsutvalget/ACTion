package utils;

import models.Activation;

import java.util.Comparator;

public class ActivationSorter implements Comparator<Activation> {
    @Override
    public int compare(Activation o1, Activation o2) {
        if(o1 == o2 || o1.getKey().equals(o2.getKey())) {
            return 0;
        }
        return o1.getWeight() - o2.getWeight();
    }
}
