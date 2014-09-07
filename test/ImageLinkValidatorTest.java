import org.junit.Test;
import play.api.Play;
import utils.imaging.ImageLinkValidator;
import play.test.FakeApplication;

import java.awt.*;
import java.io.File;

import static org.fest.assertions.Assertions.assertThat;

public class ImageLinkValidatorTest {

    @Test
    public void tooWideTest() {
        ImageLinkValidator validator = new ImageLinkValidator(new Dimension(300, 200));
        ImageLinkValidator.StatusMessage message = validator.validate("http://puertoricohotelreservations.com/wp-content/uploads/2011/11/riomargolf-1000x400.jpg");
        assertThat(!message.isSuccess());
    }

    @Test
    public void tooTallTest() {
        ImageLinkValidator validator = new ImageLinkValidator(new Dimension(300, 200));
        ImageLinkValidator.StatusMessage message = validator.validate("http://puertoricohotelreservations.com/wp-content/uploads/2011/11/riomargolf-1000x400.jpg");
        assertThat(!message.isSuccess());
    }

    @Test
    public void wrongAspectTest() {
        ImageLinkValidator validator = new ImageLinkValidator(new Dimension(1100, 200), new Dimension(600, 400));
        ImageLinkValidator.StatusMessage message = validator.validate("http://puertoricohotelreservations.com/wp-content/uploads/2011/11/riomargolf-1000x400.jpg");
        assertThat(!message.isSuccess());
    }

    @Test
    public void perfectTest() {
        ImageLinkValidator validator = new ImageLinkValidator(new Dimension(1000, 400));
        ImageLinkValidator.StatusMessage message = validator.validate("http://puertoricohotelreservations.com/wp-content/uploads/2011/11/riomargolf-1000x400.jpg");
        assertThat(message.isSuccess());
    }

    @Test
    public void almostTooWideTest() {
        ImageLinkValidator validator = new ImageLinkValidator(new Dimension(775, 400), new Dimension(1100, 400));
        ImageLinkValidator.StatusMessage message = validator.validate("http://puertoricohotelreservations.com/wp-content/uploads/2011/11/riomargolf-1000x400.jpg");
        assertThat(message.isSuccess());
    }

    @Test
    public void fallsWithinTest() {
        ImageLinkValidator validator = new ImageLinkValidator(new Dimension(775, 300), new Dimension(1100, 500), true);
        ImageLinkValidator.StatusMessage message = validator.validate("http://puertoricohotelreservations.com/wp-content/uploads/2011/11/riomargolf-1000x400.jpg");
        assertThat(message.isSuccess());
    }

    @Test
    public void testFileSupport() {
        ImageLinkValidator validator = new ImageLinkValidator(new Dimension(32, 32));
        File f = new File("public/images/favicon-32x32.png");
        ImageLinkValidator.StatusMessage message = validator.validate(f);
        assertThat(message.isSuccess());
    }

}
