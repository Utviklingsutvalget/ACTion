package utils.imaging;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ImageLinkValidator {

    public static final String[] ALLOWED_TYPES = {
            "jpeg", "jpg", "png", "svg"
    };
    public static final Double ALLOWED_DEVIATION = 0.25;
    private final Dimension minimum;
    private final Dimension maximum;
    private final boolean enforceStrict;

    public ImageLinkValidator(Dimension dimension) {
        minimum = maximum = dimension;
        enforceStrict = true;
    }

    public ImageLinkValidator(Dimension minimum, Dimension maximum) {
        this(minimum, maximum, false);
    }

    public ImageLinkValidator(Dimension minimum, Dimension maximum, boolean enforceStrict) {
        this.minimum = minimum;
        this.maximum = maximum;
        this.enforceStrict = enforceStrict;
    }

    public StatusMessage validate(String imageUrl) {
        List<String> typeList = Arrays.asList(ALLOWED_TYPES);
        StatusMessage message;
        try {
            URL url = new URL(imageUrl);
            ImageInputStream in = ImageIO.createImageInputStream(url.openStream());

            if(in == null) {
                return new StatusMessage(false, "Kunne ikke lese bildet");
            }

            final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
            if (readers.hasNext()) {
                ImageReader reader = readers.next();
                reader.setInput(in);
                Dimension imageDimension = new Dimension(reader.getWidth(0), reader.getHeight(0));

                message = verifyImageDimension(imageDimension);
                if(!message.isSuccess()) {
                    in.close();
                    return message;
                }
                String contentType = reader.getFormatName();
                in.close();
                if(contentType == null || contentType.equals("")) {
                    return new StatusMessage(false, "Kunne ikke lese bildetypen");
                } else if(typeList.contains(contentType.toLowerCase())) {
                    return new StatusMessage(true, null);
                } else {
                    return new StatusMessage(false, "Bildetypen " + contentType.toLowerCase() + " er ikke støttet");
                }
            } else {
                return new StatusMessage(false, "Kunne ikke finne noe bilde på den linken");
            }
        } catch (MalformedURLException e) {
            return new StatusMessage(false, "Linken peker ikke til en gyldig nettaddresse");
        } catch (IOException e) {
            return new StatusMessage(false, "Kunne ikke bekrefte bildet. Er du sikker på at linken er riktig?");
        }
    }

    private StatusMessage verifyImageDimension(Dimension imageDimension) {
        double ratio = imageDimension.getWidth() / imageDimension.getHeight();

        if(imageDimension.getWidth() > maximum.getWidth()) {
            return new StatusMessage(false, "Bildet du forsøkte å bruke er for bredt");
        } else if(imageDimension.getHeight() > maximum.getHeight()) {
            return new StatusMessage(false, "Bildet du forsøkte å bruke er for høyt");
        } else if(imageDimension.getWidth() < minimum.getWidth()) {
            return new StatusMessage(false, "Bildet du forsøkte å bruke er for smalt");
        } else if(imageDimension.getHeight() < minimum.getHeight()) {
            return new StatusMessage(false, "Bildet du forsøkte å bruke er for lavt");
        }

        double minRatio = minimum.getWidth() / minimum.getHeight();
        double maxRatio = maximum.getWidth() / maximum.getHeight();
        double preferredRatio = (maxRatio + minRatio) / 2;

        if(enforceStrict && preferredRatio != ratio) {
            return new StatusMessage(false, "Bildet du forsøkte å bruke faller ikke innenfor de angitte størrelsesforholdene");
        } else if(ratio > maxRatio+ALLOWED_DEVIATION || ratio > preferredRatio+ALLOWED_DEVIATION) {
            return new StatusMessage(false, "Bildet du forsøkte å bruke er for bredt i forhold til høyden");
        } else if(ratio < minRatio-ALLOWED_DEVIATION || ratio < preferredRatio-ALLOWED_DEVIATION) {
            return new StatusMessage(false, "Bildet du forsøkte å bruke er for smalt i forhold til høyden");
        }

        return new StatusMessage(true, "");
    }


    public static class StatusMessage {

        private final boolean success;
        private final String message;

        public StatusMessage(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
    }
}
