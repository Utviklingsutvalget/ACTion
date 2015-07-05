package helpers.formatters;

import models.UploadedFile;
import play.data.format.Formatters;

import java.text.ParseException;
import java.util.Locale;
import java.util.UUID;

public class UploaderFileFormatter extends Formatters.SimpleFormatter<UploadedFile> {
    @Override
    public UploadedFile parse(final String s, final Locale locale) throws ParseException {
        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setId(UUID.fromString(s));
        return uploadedFile;
    }

    @Override
    public String print(final UploadedFile uploadedFile, final Locale locale) {
        return String.valueOf(uploadedFile.getId());
    }
}
