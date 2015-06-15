import org.junit.Test;
import utils.imaging.ImageLinkValidator;

import java.awt.*;

import static org.junit.Assert.assertTrue;

public class ImageLinkValidatorTest {

    @Test
    public void tooWideTest() {
        ImageLinkValidator validator = new ImageLinkValidator(new Dimension(300, 200));
        ImageLinkValidator.StatusMessage message = validator.validate("http://puertoricohotelreservations.com/wp-content/uploads/2011/11/riomargolf-1000x400.jpg");
        assertTrue(!message.isSuccess());
    }

    @Test
    public void tooTallTest() {
        ImageLinkValidator validator = new ImageLinkValidator(new Dimension(300, 200));
        ImageLinkValidator.StatusMessage message = validator.validate("http://puertoricohotelreservations.com/wp-content/uploads/2011/11/riomargolf-1000x400.jpg");
        assertTrue(!message.isSuccess());
    }

    @Test
    public void wrongAspectTest() {
        ImageLinkValidator validator = new ImageLinkValidator(new Dimension(1100, 200), new Dimension(600, 400));
        ImageLinkValidator.StatusMessage message = validator.validate("http://puertoricohotelreservations.com/wp-content/uploads/2011/11/riomargolf-1000x400.jpg");
        assertTrue(!message.isSuccess());
    }

    @Test
    public void perfectTest() {
        ImageLinkValidator validator = new ImageLinkValidator(new Dimension(1000, 400));
        ImageLinkValidator.StatusMessage message = validator.validate("http://puertoricohotelreservations.com/wp-content/uploads/2011/11/riomargolf-1000x400.jpg");
        assertTrue(message.isSuccess());
    }

    @Test
    public void almostTooWideTest() {
        ImageLinkValidator validator = new ImageLinkValidator(new Dimension(775, 400), new Dimension(1100, 400));
        ImageLinkValidator.StatusMessage message = validator.validate("http://puertoricohotelreservations.com/wp-content/uploads/2011/11/riomargolf-1000x400.jpg");
        assertTrue(message.isSuccess());
    }

    @Test
    public void fallsWithinTest() {
        ImageLinkValidator validator = new ImageLinkValidator(new Dimension(775, 300), new Dimension(1100, 500), true);
        ImageLinkValidator.StatusMessage message = validator.validate("http://puertoricohotelreservations.com/wp-content/uploads/2011/11/riomargolf-1000x400.jpg");
        assertTrue(message.isSuccess());
    }

}
