package utils;

import controllers.OAuth2;
import models.User;
import play.Logger;

import static play.mvc.Controller.session;

public class Authorize {

    public static class UserSession {

        private User user;
        private Long expire;

        public UserSession() throws SessionException {

            if(!sessionsExists())
                throw new SessionException("Session are not set");

            this.user = User.findById(session("id"));
            this.expire = Long.parseLong(session("expires"));
        }

        public boolean isLoggedIn() {

            return !hasExpired();
        }

        private boolean hasExpired() {

            long now = System.currentTimeMillis();
            long secondsLeft = (expire - now) / 1000;

            if(secondsLeft >= 0)
                return false;

            endSession();
            return true;
        }

        public Long getSecondsLeft() {

            long now = System.currentTimeMillis();
            return (expire - now) / 1000;
        }

        private boolean sessionsExists() {
            return(session().containsKey("id") && session().containsKey("expires"));
        }

        private void endSession() {
            OAuth2.destroySessions();
        }

        public User getUser() throws SessionException {

            if(!isLoggedIn())
                throw new SessionException("User is not logged in");

            return user;
        }
    }

    public static class SessionException extends Exception {

        private String exception;

        public SessionException() {
            super();
        }

        public SessionException(String exception) {
            super(exception);
            this.exception = exception;
        }

        public SessionException(Throwable cause) {
            super(cause);
        }

        @Override
        public String toString() {
            return exception;
        }

        @Override
        public String getMessage() {
            return exception;
        }
    }
}
