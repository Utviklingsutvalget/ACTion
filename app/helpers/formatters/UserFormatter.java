package helpers.formatters;

import models.User;
import play.data.format.Formatters;
import utils.Constants;

import java.text.ParseException;
import java.util.Locale;

public class UserFormatter extends Formatters.SimpleFormatter<User> {
    @Override
    public User parse(final String s, final Locale locale) throws ParseException {
        String email = s + Constants.EMAIL_SUFFIX;
        User user = new User();
        user.setEmail(email);
        return user;
    }

    @Override
    public String print(final User user, final Locale locale) {
        return user.getEmail();
    }
}
