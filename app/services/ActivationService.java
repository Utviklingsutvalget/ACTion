package services;

import com.avaje.ebean.Ebean;
import models.Activation;

public class ActivationService {
    public void save(final Activation activation) {
        Ebean.save(activation);
    }
}
