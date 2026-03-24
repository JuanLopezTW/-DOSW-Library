package edu.eci.dosw.tdd.controller;

import edu.eci.dosw.tdd.controller.dto.LoanDTO;
import edu.eci.dosw.tdd.controller.mapper.LoanMapper;
import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.exception.LoanLimitExeededException;
import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanControllerTest {

    private LoanController loanController;
    private LoanService loanService;
    private LoanMapper loanMapper;

    @BeforeEach
    void setUp() {
        loanService = Mockito.mock(LoanService.class);
        loanMapper = Mockito.mock(LoanMapper.class);
        loanController = new LoanController(loanService, loanMapper);
    }

    @Test
    void testCreateLoanSuccessfully() {
        Book book = new Book("El Principito", "Antoine", 1L);
        User user = new User("Juan", 1L);
        Loan loan = new Loan(book, user, new Date(), Loan.LoanStatus.ACTIVE, null);
        LoanDTO loanDTO = new LoanDTO(1L, "El Principito", 1L, "Juan", new Date(), Loan.LoanStatus.ACTIVE, null);
        when(loanService.createLoan(1L, 1L)).thenReturn(loan);
        when(loanMapper.toDTO(loan)).thenReturn(loanDTO);
        ResponseEntity<LoanDTO> response = loanController.createLoan(1L, 1L);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testReturnLoanSuccessfully() {
        ResponseEntity<Void> response = loanController.returnLoan(1L, 1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testGetAllLoansSuccessfully() {
        when(loanService.getAllLoans()).thenReturn(List.of());
        ResponseEntity<List<LoanDTO>> response = loanController.getAllLoans();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetLoansByUserSuccessfully() {
        when(loanService.getLoansByUser(1L)).thenReturn(List.of());
        ResponseEntity<List<LoanDTO>> response = loanController.getLoansByUser(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testCreateLoanUserNotFound() {
        when(loanService.createLoan(99L, 1L)).thenThrow(new UserNotFoundException(99L));
        assertThrows(UserNotFoundException.class, () -> loanController.createLoan(99L, 1L));
    }

    @Test
    void testCreateLoanBookNotFound() {
        when(loanService.createLoan(1L, 99L)).thenThrow(new BookNotAvailableException(99L));
        assertThrows(BookNotAvailableException.class, () -> loanController.createLoan(1L, 99L));
    }

    @Test
    void testCreateLoanLimitExceeded() {
        when(loanService.createLoan(1L, 1L)).thenThrow(new LoanLimitExeededException(1L));
        assertThrows(LoanLimitExeededException.class, () -> loanController.createLoan(1L, 1L));
    }
}
