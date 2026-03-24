package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.exception.LoanLimitExeededException;
import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.persistence.mapper.BookPersistenceMapper;
import edu.eci.dosw.tdd.persistence.mapper.UserPersistenceMapper;
import edu.eci.dosw.tdd.persistence.repository.BookRepository;
import edu.eci.dosw.tdd.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanServiceTest {

    private LoanService loanService;
    private BookService bookService;
    private UserService userService;
    private Book book1;
    private Book book2;
    private Book book3;
    private Book book4;
    private User user1;

    @BeforeEach
    void setUp() {

        bookService = Mockito.mock(BookService.class);
        userService = Mockito.mock(UserService.class);
        loanService = new LoanService(bookService, userService);

        book1 = new Book("El Principito", "Antoine", 1L, 3);
        book2 = new Book("Cien Anos de Soledad", "Garcia Marquez", 2L, 3);
        book3 = new Book("1984", "Orwell", 3L, 3);
        book4 = new Book("Don Quijote", "Cervantes", 4L, 3);
        user1 = new User("Juan", 1L);

        when(bookService.getBook(1L)).thenReturn(book1);
        when(bookService.getBook(2L)).thenReturn(book2);
        when(bookService.getBook(3L)).thenReturn(book3);
        when(bookService.getBook(4L)).thenReturn(book4);
        when(userService.getUser(1L)).thenReturn(user1);
    }

    @Test
    void testCreateLoanSuccessfully() {
        Loan loan = loanService.createLoan(1L, 1L);
        assertNotNull(loan);
        assertEquals(Loan.LoanStatus.ACTIVE, loan.getStatus());
    }

    @Test
    void testCreateLoanDecreasesCopies() {
        loanService.createLoan(1L, 1L);
        verify(bookService, times(1)).decreaseCopy(1L);
    }

    @Test
    void testReturnLoanSuccessfully() {
        loanService.createLoan(1L, 1L);
        loanService.returnLoan(1L, 1L);
        assertEquals(Loan.LoanStatus.RETURNED, loanService.getAllLoans().get(0).getStatus());
    }

    @Test
    void testReturnLoanIncreasesCopies() {
        loanService.createLoan(1L, 1L);
        loanService.returnLoan(1L, 1L);
        verify(bookService, times(1)).increaseCopy(1L);
    }

    @Test
    void testGetAllLoans() {
        loanService.createLoan(1L, 1L);
        loanService.createLoan(1L, 2L);
        assertEquals(2, loanService.getAllLoans().size());
    }

    @Test
    void testGetLoansByUser() {
        loanService.createLoan(1L, 1L);
        loanService.createLoan(1L, 2L);
        assertEquals(2, loanService.getLoansByUser(1L).size());
    }

    @Test
    void testCreateLoanUserNotFound() {
        when(userService.getUser(99L)).thenThrow(new UserNotFoundException(99L));
        assertThrows(UserNotFoundException.class, () -> loanService.createLoan(99L, 1L));
    }

    @Test
    void testCreateLoanBookNotFound() {
        when(bookService.getBook(99L)).thenThrow(new BookNotAvailableException(99L));
        assertThrows(BookNotAvailableException.class, () -> loanService.createLoan(1L, 99L));
    }

    @Test
    void testCreateLoanLimitExceeded() {
        loanService.createLoan(1L, 1L);
        loanService.createLoan(1L, 2L);
        loanService.createLoan(1L, 3L);
        assertThrows(LoanLimitExeededException.class, () -> loanService.createLoan(1L, 4L));
    }

    @Test
    void testCreateLoanBookNotAvailable() {
        when(bookService.getBook(1L)).thenReturn(book1);
        doThrow(new BookNotAvailableException(1L)).when(bookService).decreaseCopy(1L);
        assertThrows(BookNotAvailableException.class, () -> loanService.createLoan(1L, 1L));
    }
}