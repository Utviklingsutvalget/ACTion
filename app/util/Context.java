package util;

import models.User;
import play.db.ebean.Model;

/**
 * An objective representation of what a user intends to do.
 */
public class Context {

    private final User sender;
    private final Model resource;
    private final Action action;

    /**
     *
     * @param sender The user whose action we wish to authorize
     * @param resource The resource the user wishes to perform an action upon
     * @param action The type of action the user wishes to perform
     */
    public Context(User sender, Model resource, Action action) {
        this.sender = sender;
        this.resource = resource;
        this.action = action;
    }

    /**
     * @return The user who originated the request.
     */
    public User getSender() {
        return sender;
    }

    /**
     * @return The resource the user wishes to perform the request upon. If null
     */
    public Model getResource() {
        return resource;
    }

    public Action getAction() {
        return action;
    }
}
