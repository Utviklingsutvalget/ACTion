import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.PlayAuthenticate.Resolver;
import com.feth.play.module.pa.exceptions.AccessDeniedException;
import com.feth.play.module.pa.exceptions.AuthException;
import controllers.routes;
import helpers.formatters.LocationFormatter;
import helpers.formatters.UploaderFileFormatter;
import helpers.formatters.UserFormatter;
import models.Location;
import models.UploadedFile;
import models.User;
import play.Application;
import play.GlobalSettings;
import play.data.format.Formatters;
import play.libs.F;
import play.mvc.Call;
import play.mvc.Http;
import play.mvc.Result;
import play.twirl.api.Content;

import static play.mvc.Results.notFound;

public class Global extends GlobalSettings {

    @Override
    public F.Promise<Result> onHandlerNotFound(Http.RequestHeader request) {
        return F.Promise.<Result>pure(notFound(
                (Content) views.html.error.render("Kunne ikke finne " + request.uri())
        ));
    }

    @Override
    public void onStart(final Application app) {
        Formatters.register(Location.class, new LocationFormatter());
        Formatters.register(User.class, new UserFormatter());
        Formatters.register(UploadedFile.class, new UploaderFileFormatter());
        PlayAuthenticate.setResolver(new Resolver() {

            @Override
            public Call login() {
                // Your login page
                return routes.Application.index();
            }

            @Override
            public Call afterAuth() {
                // The user will be redirected to this page after authentication
                // if no original URL was saved
                return routes.Application.index();
            }

            @Override
            public Call afterLogout() {
                return routes.Application.index();
            }

            @Override
            public Call auth(final String provider) {
                // You can provide your own authentication implementation,
                // however the default should be sufficient for most cases
                return routes.Application.authenticate(provider);
            }

            @Override
            public Call onException(final AuthException e) {
                if (e instanceof AccessDeniedException) {
                    return routes.Application
                            .oAuthDenied(((AccessDeniedException) e)
                                    .getProviderKey());
                }

                // more custom problem handling here...

                return super.onException(e);
            }

            @Override
            public Call askLink() {
                // We don't support moderated account linking in this sample.
                // See the play-authenticate-usage project for an example
                return null;
            }

            @Override
            public Call askMerge() {
                // We don't support moderated account merging in this sample.
                // See the play-authenticate-usage project for an example
                return null;
            }
        });
    }

}
