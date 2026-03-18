package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.exception.LoanLimitExeededException;
import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        bookService = new BookService();
        userService = new UserService();
        loanService = new LoanService(bookService, userService);

        book1 = new Book("El Principito", "Antoine", 1L);
        book2 = new Book("Cien Anos de Soledad", "Garcia Marquez", 2L);
        book3 = new Book("1984", "Orwell", 3L);
        book4 = new Book("Don Quijote", "Cervantes", 4L);
        user1 = new User("Juan", 1L);

        bookService.addBook(book1, 3);
        bookService.addBook(book2, 3);
        bookService.addBook(book3, 3);
        bookService.addBook(book4, 3);
        userService.addUser(user1);
    }

    @Test
    void testCreateLoanSuccessfully() {
        Loan loan = loanService.createLoan(1L, 1L);
        assertNotNull(loan);
        assertEquals("ACTIVE", loan.getStatus());
    }

    @Test
    void testCreateLoanDecreasesCopies() {
        loanService.createLoan(1L, 1L);
        assertEquals(2, bookService.getAllBooks().get(book1));
    }

    @Test
    void testReturnLoanSuccessfully() {
        loanService.createLoan(1L, 1L);
        loanService.returnLoan(1L, 1L);
        assertEquals("RETURNED", loanService.getAllLoans().get(0).getStatus());
    }

    @Test
    void testReturnLoanIncreasesCopies() {
        loanService.createLoan(1L, 1L);
        loanService.returnLoan(1L, 1L);
        assertEquals(3, bookService.getAllBooks().get(book1));
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
        assertThrows(UserNotFoundException.class, () -> loanService.createLoan(99L, 1L));
    }

    @Test
    void testCreateLoanBookNotFound() {
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
        bookService.decreaseCopy(1L);
        bookService.decreaseCopy(1L);
        bookService.decreaseCopy(1L);
        assertThrows(BookNotAvailableException.class, () -> loanService.createLoan(1L, 1L));
    }
}
