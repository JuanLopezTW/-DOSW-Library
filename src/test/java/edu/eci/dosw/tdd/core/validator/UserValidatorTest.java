package edu.eci.dosw.tdd.core.validator;

import edu.eci.dosw.tdd.core.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {

    private UserValidator userValidator;

    @BeforeEach
    void setUp() {
        userValidator = new UserValidator();
    }

    @Test
    void testValidateUserSuccessfully() {
        User user = new User("Juan", 1L);
        assertDoesNotThrow(() -> userValidator.validate(user));
    }

    @Test
    void testValidateUserNull() {
        assertThrows(IllegalArgumentException.class, () -> userValidator.validate(null));
    }

    @Test
    void testValidateUserNameEmpty() {
        User user = new User("", 1L);
        assertThrows(IllegalArgumentException.class, () -> userValidator.validate(user));
    }

    @Test
    void testValidateUserNameNull() {
        User user = new User(null, 1L);
        assertThrows(IllegalArgumentException.class, () -> userValidator.validate(user));
    }
}
