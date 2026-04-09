package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.repository.LoanRepository;
import edu.eci.dosw.tdd.core.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserService userService;
    private UserRepository userRepository;
    private LoanRepository loanRepository;
    private PasswordEncoder passwordEncoder;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        loanRepository = Mockito.mock(LoanRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserService(userRepository, loanRepository, passwordEncoder);

        user1 = new User("Juan", 1L);
        user2 = new User("Maria", 2L);
    }

    @Test
    void testAddUserSuccessfully() {
        when(passwordEncoder.encode(any())).thenReturn("hashedPassword");
        userService.addUser(user1);
        verify(userRepository, times(1)).save(user1);
    }

    @Test
    void testGetUserSuccessfully() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        User found = userService.getUser(1L);
        assertEquals(user1, found);
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));
        assertEquals(2, userService.getAllUsers().size());
    }

    @Test
    void testDeleteUserSuccessfully() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(loanRepository.findByUserIdAndStatus(1L, Loan.LoanStatus.ACTIVE.name()))
                .thenReturn(List.of());
        userService.deleteUser(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUserWithActiveLoans() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(loanRepository.findByUserIdAndStatus(1L, Loan.LoanStatus.ACTIVE.name()))
                .thenReturn(List.of(new Loan()));
        assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(1L));
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