package helpers.formatters;

import models.Location;
import play.data.format.Formatters;

import java.text.ParseException;
import java.util.Locale;

public class LocationFormatter extends Formatters.SimpleFormatter<Location> {

    @Override
    public Location parse(final String s, final Locale locale) throws ParseException {
        return new Location(Long.parseLong(s));
    }

    @Override
    public String print(final Location location, final Locale locale) {
        return String.valueOf(location.getId());
    }
}
