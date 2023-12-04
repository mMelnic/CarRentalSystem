package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import carrental.models.User;

public class UserTest {
        @Test
    public void testValidUser() {
        User validUser = new User("john_snow", "password123", "John Snow", "john@example.com");
        assertTrue(validUser.isValidUser());
    }

    @Test
    public void testInvalidUsername() {
        User invalidUsernameUser = new User("jo", "password123", "John Snow", "john@example.com");
        assertFalse(invalidUsernameUser.isValidUser());
    }

    @Test
    public void testInvalidPassword() {
        User invalidPasswordUser = new User("john_snow", "pass", "John Snow", "john@example.com");
        assertFalse(invalidPasswordUser.isValidUser());
    }

    @Test
    public void testInvalidFullName() {
        User invalidFullNameUser = new User("john_snow", "password123", "John123", "john@example.com");
        assertFalse(invalidFullNameUser.isValidUser());
    }

    @Test
    public void testInvalidEmail() {
        User invalidEmailUser = new User("john_snow", "password123", "John Snow", "john@example");
        assertFalse(invalidEmailUser.isValidUser());
    }

    @Test
    public void testValidEmailWithDifferentExtensions() {
        String[] validExtensions = {"com", "net", "org", "edu"};
        for (String extension : validExtensions) {
            User validEmailUser = new User("john_snow", "password123", "John Snow", "john@example." + extension);
            assertTrue(validEmailUser.isValidUser());
        }
    }

    @Test
    public void testInvalidEmailWithWrongExtension() {
        User invalidEmailUser = new User("john_snow", "password123", "John Snow", "john@example.xyz");
        assertFalse(invalidEmailUser.isValidUser());
    }

    @Test
    public void testNullFields() {
        User nullFieldsUser = new User(null, null, null, null);
        assertFalse(nullFieldsUser.isValidUser());
    }

    @Test
    public void testEmptyFields() {
        User emptyFieldsUser = new User("", "", "", "");
        assertFalse(emptyFieldsUser.isValidUser());
    }

    @Test
    public void testValidFullNameWithApostrophe() {
        User validFullNameUser = new User("john_snow", "password123", "John O'Snow", "john@example.com");
        assertTrue(validFullNameUser.isValidUser());
    }
}
