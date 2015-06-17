package models;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {

    private User user;

    @Before
    public void setup() {
        user = new User("1234", "TestFirst", "TestLast", "tester@testing.test", "http://picture.pic/picture.png");
    }

    @Test
    public void testEquals() throws Exception {
        User other = new User("1234", "TestFirst", "TestLast", "tester@testing.test", "http://picture.pic/picture.png");

        assertTrue("Equals test is true with completely identical user objects", user.equals(other));
    }
}
