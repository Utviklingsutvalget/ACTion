package services;

import com.avaje.ebean.Ebean;
import com.feth.play.module.pa.service.UserServicePlugin;
import com.feth.play.module.pa.user.AuthUser;
import com.feth.play.module.pa.user.AuthUserIdentity;
import com.google.inject.Inject;
import com.timgroup.jgravatar.Gravatar;
import com.timgroup.jgravatar.GravatarRating;
import models.User;
import play.Application;
import play.db.ebean.Transactional;

public class UserService extends UserServicePlugin {

    @Inject
    public UserService(final Application app) {
        super(app);
    }

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

    @Override
    public Object save(final AuthUser authUser) {
        final boolean isLinked = findById(authUser.getId()) != null;
        System.out.println("Saving");
        if(!isLinked) {
            User bean = new User(authUser);
            Ebean.save(bean);
            return bean.getId();
        } else {
            return null;
        }
    }

    @Override
    public Object getLocalIdentity(final AuthUserIdentity authUserIdentity) {
        final User user = findById(authUserIdentity.getId());
        System.out.println(user);
        return user != null ? user.getId() : null;
    }

    @Override
    public AuthUser merge(final AuthUser authUser, final AuthUser authUser1) {
        return null;
    }

    @Override
    public AuthUser link(final AuthUser authUser, final AuthUser authUser1) {
        return null;
    }
}
