package helpers;

import com.avaje.ebean.Ebean;
import com.timgroup.jgravatar.Gravatar;
import com.timgroup.jgravatar.GravatarDefaultImage;
import com.timgroup.jgravatar.GravatarRating;
import models.User;
import play.db.ebean.Model;
import play.db.ebean.Transactional;

public class UserService {

    private static Model.Finder<String, User> find = new Model.Finder<>(String.class, User.class);

    public static User findByEmail(String email) {
        return find.where().eq("email", email).findUnique();
    }

    public static void setupGravatar(User user, String defaultImage) {
        Gravatar gravatar = new Gravatar();
        gravatar = gravatar.setSize(350);
        gravatar = gravatar.setRating(GravatarRating.PARENTAL_GUIDANCE_SUGGESTED);
        String url = gravatar.getUrl(user.getEmail());
        url = url.replace("d=404", "d=" + defaultImage);
        user.setGravatarUrl(url);
    }

    public static User findById(String id) {
        return find.where().eq("id", id).findUnique();
    }

    public static User findByName(String firstName, String lastName) {
        return find.where().eq("firstName", firstName).eq("lastName", lastName).findUnique();
    }

    public static boolean userExists(String id) {
        return find.where().eq("id", id).findRowCount() != 0;
    }

    @Transactional
    public static void save(User user) {

        if (!userExists(user.getId()))
            Ebean.save(user);
    }

    @Transactional
    public static void update(User user) {
        Ebean.update(user);
    }

}
