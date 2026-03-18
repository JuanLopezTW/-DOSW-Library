package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService userService;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        userService = new UserService();
        user1 = new User("Juan", 1L);
        user2 = new User("Maria", 2L);
    }

    @Test
    void testAddUserSuccessfully() {
        userService.addUser(user1);
        assertEquals(1, userService.getAllUsers().size());
    }

    @Test
    void testGetUserSuccessfully() {
        userService.addUser(user1);
        User found = userService.getUser(1L);
        assertEquals(user1, found);
    }

    @Test
    void testGetAllUsers() {
        userService.addUser(user1);
        userService.addUser(user2);
        assertEquals(2, userService.getAllUsers().size());
    }

    @Test
    void testDeleteUserSuccessfully() {
        userService.addUser(user1);
        userService.deleteUser(1L);
        assertEquals(0, userService.getAllUsers().size());
    }
    @Test
    void testGetUserNotFound() {
        assertThrows(UserNotFoundException.class, () -> userService.getUser(99L));
    }

    @Test
    void testDeleteUserNotFound() {
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(99L));
    }
}