package edu.eci.dosw.tdd.controller;

import edu.eci.dosw.tdd.controller.dto.UserDTO;
import edu.eci.dosw.tdd.controller.mapper.UserMapper;
import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.service.UserService;
import edu.eci.dosw.tdd.core.validator.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private UserController userController;
    private UserService userService;
    private UserValidator userValidator;
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userValidator = Mockito.mock(UserValidator.class);
        userMapper = Mockito.mock(UserMapper.class);
        userController = new UserController(userService, userValidator, userMapper);
    }

    @Test
    void testGetAllUsersSuccessfully() {
        when(userService.getAllUsers()).thenReturn(List.of());
        ResponseEntity<List<UserDTO>> response = userController.getAllUsers();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testAddUserSuccessfully() {
        UserDTO userDTO = new UserDTO(1L, "Juan", "juanlopez", "1234", "USER");
        User user = new User("Juan", 1L);
        when(userMapper.toModel(userDTO)).thenReturn(user);
        ResponseEntity<Void> response = userController.addUser(userDTO);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testGetUserSuccessfully() {
        User user = new User("Juan", 1L);
        UserDTO userDTO = new UserDTO(1L, "Juan", "juanlopez", "1234", "USER");
        when(userService.getUser(1L)).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(userDTO);
        ResponseEntity<UserDTO> response = userController.getUser(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    void testDeleteUserSuccessfully() {
        ResponseEntity<Void> response = userController.deleteUser(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testGetUserNotFound() {
        when(userService.getUser(99L)).thenThrow(new UserNotFoundException(99L));
        assertThrows(UserNotFoundException.class, () -> userController.getUser(99L));
    }

    @Test
    void testAddUserValidationFails() {
        UserDTO userDTO = new UserDTO(1L, "", null, null, null);
        User user = new User("", 1L);
        when(userMapper.toModel(userDTO)).thenReturn(user);
        doThrow(new IllegalArgumentException("User name cannot be empty"))
                .when(userValidator).validate(user);
        assertThrows(IllegalArgumentException.class, () -> userController.addUser(userDTO));
    }
}

