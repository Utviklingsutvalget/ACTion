package utils;

import models.Activation;

import java.util.Comparator;

public class ActivationSorter implements Comparator<Activation> {
    @Override
    public int compare(Activation o1, Activation o2) {
        if(o1 == o2 || o1.key.equals(o2.key)) {
            return 0;
        }
        return o1.weight - o2.weight;
    }
}
