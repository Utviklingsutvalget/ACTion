package utils;

import controllers.OAuth2;
import models.User;

import static play.mvc.Controller.session;

public class Authorization {

    public static User authorizeUserSession() {

        if(session().containsKey("id") && session().containsKey("expires")) {

            long expire = Long.parseLong(session().get("expires"));
            long now = System.currentTimeMillis();

            long secondsLeft = (expire - now) / 1000;

            if (secondsLeft < 0) {

                OAuth2.destroySessions();
                return null;
            }

            return User.findById(session().get("id"));
        }
        return null;
    }
}
