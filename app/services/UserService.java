package services;

import com.avaje.ebean.Ebean;
import com.timgroup.jgravatar.Gravatar;
import com.timgroup.jgravatar.GravatarRating;
import models.User;
import play.db.ebean.Transactional;

public class UserService {

    public User findByEmail(String email) {
        return Ebean.find(User.class).where().eq("email", email).findUnique();
    }

    public static void setupGravatar(User user, String defaultImage) {
        Gravatar gravatar = new Gravatar();
        gravatar = gravatar.setSize(350);
        gravatar = gravatar.setRating(GravatarRating.PARENTAL_GUIDANCE_SUGGESTED);
        String url = gravatar.getUrl(user.getEmail());
        url = url.replace("d=404", "d=" + defaultImage);
        user.setGravatarUrl(url);
    }

    public User findById(String id) {
        return Ebean.find(User.class).where().eq("id", id).findUnique();
    }

    public User findByName(String firstName, String lastName) {
        return Ebean.find(User.class).where().eq("firstName", firstName).eq("lastName", lastName).findUnique();
    }

    public boolean userExists(String id) {
        return Ebean.find(User.class).where().eq("id", id).findRowCount() != 0;
    }

    @Transactional
    public void save(User user) {

        if (!userExists(user.getId()))
            Ebean.save(user);
    }

    @Transactional
    public void update(User user) {
        Ebean.update(user);
    }

}
