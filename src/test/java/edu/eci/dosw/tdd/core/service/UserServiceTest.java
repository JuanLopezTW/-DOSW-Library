package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.persistence.entity.UserEntity;
import edu.eci.dosw.tdd.persistence.mapper.UserPersistenceMapper;
import edu.eci.dosw.tdd.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserService userService;
    private UserRepository userRepository;
    private UserPersistenceMapper userMapper;
    private User user1;
    private User user2;
    private UserEntity userEntity1;
    private UserEntity userEntity2;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userMapper = Mockito.mock(UserPersistenceMapper.class);
        userService = new UserService(userRepository, userMapper);

        user1 = new User("Juan", 1L);
        user2 = new User("Maria", 2L);
        userEntity1 = new UserEntity(1L, "Juan", "juan123", "1234", UserEntity.Role.USER);
        userEntity2 = new UserEntity(2L, "Maria", "maria123", "1234", UserEntity.Role.USER);
    }

    @Test
    void testAddUserSuccessfully() {
        when(userMapper.toEntity(user1)).thenReturn(userEntity1);
        userService.addUser(user1);
        verify(userRepository, times(1)).save(userEntity1);
    }

    @Test
    void testGetUserSuccessfully() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity1));
        when(userMapper.toModel(userEntity1)).thenReturn(user1);
        User found = userService.getUser(1L);
        assertEquals(user1, found);
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(userEntity1, userEntity2));
        when(userMapper.toModel(userEntity1)).thenReturn(user1);
        when(userMapper.toModel(userEntity2)).thenReturn(user2);
        assertEquals(2, userService.getAllUsers().size());
    }

    @Test
    void testDeleteUserSuccessfully() {
        when(userRepository.existsById(1L)).thenReturn(true);
        userService.deleteUser(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUser(99L));
    }

    @Test
    void testDeleteUserNotFound() {
        when(userRepository.existsById(99L)).thenReturn(false);
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(99L));
    }
}