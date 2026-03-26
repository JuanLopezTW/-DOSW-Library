package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.exception.LoanLimitExeededException;
import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.persistence.entity.BookEntity;
import edu.eci.dosw.tdd.persistence.entity.LoanEntity;
import edu.eci.dosw.tdd.persistence.entity.UserEntity;
import edu.eci.dosw.tdd.persistence.mapper.LoanPersistenceMapper;
import edu.eci.dosw.tdd.persistence.repository.BookRepository;
import edu.eci.dosw.tdd.persistence.repository.LoanRepository;
import edu.eci.dosw.tdd.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanServiceTest {

    private LoanService loanService;
    private BookService bookService;
    private UserService userService;
    private LoanRepository loanRepository;
    private BookRepository bookRepository;
    private UserRepository userRepository;
    private LoanPersistenceMapper loanMapper;

    private Book book1;
    private Book book2;
    private Book book3;
    private Book book4;
    private User user1;
    private BookEntity bookEntity1;
    private BookEntity bookEntity2;
    private BookEntity bookEntity3;
    private BookEntity bookEntity4;
    private UserEntity userEntity1;

    @BeforeEach
    void setUp() {
        bookService = Mockito.mock(BookService.class);
        userService = Mockito.mock(UserService.class);
        loanRepository = Mockito.mock(LoanRepository.class);
        bookRepository = Mockito.mock(BookRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        loanMapper = Mockito.mock(LoanPersistenceMapper.class);

        loanService = new LoanService(loanRepository, bookRepository, userRepository,
                loanMapper, bookService, userService);

        book1 = new Book("El Principito", "Antoine", 1L, 3);
        book2 = new Book("Cien Anos de Soledad", "Garcia Marquez", 2L, 3);
        book3 = new Book("1984", "Orwell", 3L, 3);
        book4 = new Book("Don Quijote", "Cervantes", 4L, 3);
        user1 = new User("Juan", 1L);

        bookEntity1 = new BookEntity(1L, "El Principito", "Antoine", 3, 3);
        bookEntity2 = new BookEntity(2L, "Cien Anos de Soledad", "Garcia Marquez", 3, 3);
        bookEntity3 = new BookEntity(3L, "1984", "Orwell", 3, 3);
        bookEntity4 = new BookEntity(4L, "Don Quijote", "Cervantes", 3, 3);
        userEntity1 = new UserEntity(1L, "Juan", "juan123", "1234", UserEntity.Role.USER);

        when(bookService.getBook(1L)).thenReturn(book1);
        when(bookService.getBook(2L)).thenReturn(book2);
        when(bookService.getBook(3L)).thenReturn(book3);
        when(bookService.getBook(4L)).thenReturn(book4);
        when(userService.getUser(1L)).thenReturn(user1);

        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity1));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookEntity1));
        when(bookRepository.findById(2L)).thenReturn(Optional.of(bookEntity2));
        when(bookRepository.findById(3L)).thenReturn(Optional.of(bookEntity3));
        when(bookRepository.findById(4L)).thenReturn(Optional.of(bookEntity4));

        when(loanRepository.findByUserIdAndStatus(anyLong(), anyString())).thenReturn(List.of());
    }

    @Test
    void testCreateLoanSuccessfully() {
        LoanEntity entity = new LoanEntity();
        entity.setUser(userEntity1);
        entity.setBook(bookEntity1);
        entity.setStatus(Loan.LoanStatus.ACTIVE.name());
        when(loanRepository.save(any())).thenReturn(entity);
        when(loanMapper.toModel(any())).thenReturn(new Loan(book1, user1, null, Loan.LoanStatus.ACTIVE, null));

        Loan loan = loanService.createLoan(1L, 1L);
        assertNotNull(loan);
        assertEquals(Loan.LoanStatus.ACTIVE, loan.getStatus());
    }

    @Test
    void testCreateLoanDecreasesCopies() {
        when(loanMapper.toModel(any())).thenReturn(new Loan(book1, user1, null, Loan.LoanStatus.ACTIVE, null));
        loanService.createLoan(1L, 1L);
        verify(bookService, times(1)).decreaseCopy(1L);
    }

    @Test
    void testReturnLoanSuccessfully() {
        LoanEntity entity = new LoanEntity();
        entity.setUser(userEntity1);
        entity.setBook(bookEntity1);
        entity.setStatus(Loan.LoanStatus.ACTIVE.name());

        when(loanMapper.toModel(any())).thenReturn(new Loan(book1, user1, null, Loan.LoanStatus.ACTIVE, null));
        when(loanRepository.findByUserIdAndBookIdAndStatus(1L, 1L, Loan.LoanStatus.ACTIVE.name()))
                .thenReturn(Optional.of(entity));

        loanService.createLoan(1L, 1L);
        loanService.returnLoan(1L, 1L);
        assertEquals(Loan.LoanStatus.RETURNED.name(), entity.getStatus());
    }

    @Test
    void testReturnLoanIncreasesCopies() {
        LoanEntity entity = new LoanEntity();
        entity.setUser(userEntity1);
        entity.setBook(bookEntity1);
        entity.setStatus(Loan.LoanStatus.ACTIVE.name());

        when(loanMapper.toModel(any())).thenReturn(new Loan(book1, user1, null, Loan.LoanStatus.ACTIVE, null));
        when(loanRepository.findByUserIdAndBookIdAndStatus(1L, 1L, Loan.LoanStatus.ACTIVE.name()))
                .thenReturn(Optional.of(entity));

        loanService.createLoan(1L, 1L);
        loanService.returnLoan(1L, 1L);
        verify(bookService, times(1)).increaseCopy(1L);
    }

    @Test
    void testGetAllLoans() {
        when(loanMapper.toModel(any())).thenReturn(new Loan(book1, user1, null, Loan.LoanStatus.ACTIVE, null));
        when(loanRepository.findAll()).thenReturn(List.of(new LoanEntity(), new LoanEntity()));
        assertEquals(2, loanService.getAllLoans().size());
    }

    @Test
    void testGetLoansByUser() {
        when(loanMapper.toModel(any())).thenReturn(new Loan(book1, user1, null, Loan.LoanStatus.ACTIVE, null));
        when(loanRepository.findByUserId(1L)).thenReturn(List.of(new LoanEntity(), new LoanEntity()));
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
        LoanEntity entity = new LoanEntity();
        when(loanMapper.toModel(any())).thenReturn(new Loan(book1, user1, null, Loan.LoanStatus.ACTIVE, null));
        when(loanRepository.findByUserIdAndStatus(anyLong(), anyString()))
                .thenReturn(List.of(entity, entity, entity));
        assertThrows(LoanLimitExeededException.class, () -> loanService.createLoan(1L, 1L));
    }

    @Test
    void testCreateLoanBookNotAvailable() {
        when(bookService.getBook(1L)).thenReturn(book1);
        doThrow(new BookNotAvailableException(1L)).when(bookService).decreaseCopy(1L);
        assertThrows(BookNotAvailableException.class, () -> loanService.createLoan(1L, 1L));
    }
}