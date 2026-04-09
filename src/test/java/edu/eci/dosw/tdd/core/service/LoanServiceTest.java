package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.exception.LoanLimitExeededException;
import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.repository.LoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.exception.LoanLimitExeededException;
import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.repository.LoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanServiceTest {

    private LoanService loanService;
    private LoanRepository loanRepository;
    private BookService bookService;
    private UserService userService;

    private Book book1;
    private User user1;

    @BeforeEach
    void setUp() {
        loanRepository = Mockito.mock(LoanRepository.class);
        bookService = Mockito.mock(BookService.class);
        userService = Mockito.mock(UserService.class);
        loanService = new LoanService(loanRepository, bookService, userService);

        book1 = new Book("El Principito", "Antoine", 1L, 3);
        user1 = new User("Juan", 1L);

        when(bookService.getBook(1L)).thenReturn(book1);
        when(userService.getUser(1L)).thenReturn(user1);
        when(loanRepository.findByUserIdAndStatus(anyLong(), anyString())).thenReturn(List.of());
    }


    @Test
    void testCreateLoanSuccessfully() {
        when(loanRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        Loan loan = loanService.createLoan(1L, 1L);
        assertNotNull(loan);
        assertEquals(Loan.LoanStatus.ACTIVE, loan.getStatus());
    }

    @Test
    void testCreateLoanDecreasesCopies() {
        when(loanRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        loanService.createLoan(1L, 1L);
        verify(bookService, times(1)).decreaseCopy(1L);
    }

    @Test
    void testReturnLoanSuccessfully() {
        Loan loan = new Loan(book1, user1, null, Loan.LoanStatus.ACTIVE, null);
        when(loanRepository.findByUserIdAndBookIdAndStatus(1L, 1L, Loan.LoanStatus.ACTIVE.name()))
                .thenReturn(Optional.of(loan));
        when(loanRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        loanService.returnLoan(1L, 1L);
        assertEquals(Loan.LoanStatus.RETURNED, loan.getStatus());
    }

    @Test
    void testReturnLoanIncreasesCopies() {
        Loan loan = new Loan(book1, user1, null, Loan.LoanStatus.ACTIVE, null);
        when(loanRepository.findByUserIdAndBookIdAndStatus(1L, 1L, Loan.LoanStatus.ACTIVE.name()))
                .thenReturn(Optional.of(loan));
        when(loanRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        loanService.returnLoan(1L, 1L);
        verify(bookService, times(1)).increaseCopy(1L);
    }

    @Test
    void testGetAllLoans() {
        when(loanRepository.findAll()).thenReturn(List.of(new Loan(), new Loan()));
        assertEquals(2, loanService.getAllLoans().size());
    }

    @Test
    void testGetLoansByUser() {
        when(loanRepository.findByUserId(1L)).thenReturn(List.of(new Loan(), new Loan()));
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
        when(loanRepository.findByUserIdAndStatus(anyLong(), anyString()))
                .thenReturn(List.of(new Loan(), new Loan(), new Loan()));
        assertThrows(LoanLimitExeededException.class, () -> loanService.createLoan(1L, 1L));
    }

    @Test
    void testCreateLoanBookNotAvailable() {
        doThrow(new BookNotAvailableException(1L)).when(bookService).decreaseCopy(1L);
        assertThrows(BookNotAvailableException.class, () -> loanService.createLoan(1L, 1L));
    }

    @Test
    void dadoQueExisteUnPrestamo_cuandoLoConsulto_entoncesLaConsultaEsExitosaValidandoId() {

        Loan loan = new Loan(book1, user1, new Date(), Loan.LoanStatus.ACTIVE, null);
        when(loanRepository.findAll()).thenReturn(List.of(loan));


        List<Loan> result = loanService.getAllLoans();


        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getBook().getId());
    }

    @Test
    void dadoQueNoHayPrestamos_cuandoConsulto_entoncesRetornaListaVacia() {
        when(loanRepository.findAll()).thenReturn(List.of());


        List<Loan> result = loanService.getAllLoans();

        assertTrue(result.isEmpty());
    }

    @Test
    void dadoQueNoHayPrestamos_cuandoCreoUnPrestamo_entoncesLaCreacionEsExitosa() {

        Loan loan = new Loan(book1, user1, new Date(), Loan.LoanStatus.ACTIVE, null);
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);

        Loan result = loanService.createLoan(1L, 1L);

        assertNotNull(result);
        assertEquals(Loan.LoanStatus.ACTIVE, result.getStatus());
        verify(loanRepository, times(1)).save(any(Loan.class));
    }

    @Test
    void dadoQueExisteUnPrestamo_cuandoLoDevuelvo_entoncesLaEliminacionEsExitosa() {
        Loan loan = new Loan(book1, user1, new Date(), Loan.LoanStatus.ACTIVE, null);
        when(loanRepository.findByUserIdAndBookIdAndStatus(1L, 1L, Loan.LoanStatus.ACTIVE.name()))
                .thenReturn(Optional.of(loan));
        when(loanRepository.save(any(Loan.class))).thenAnswer(i -> i.getArgument(0));


        assertDoesNotThrow(() -> loanService.returnLoan(1L, 1L));

        verify(loanRepository, times(1)).save(any(Loan.class));
        assertEquals(Loan.LoanStatus.RETURNED, loan.getStatus());
    }

    @Test
    void dadoQueExisteUnPrestamo_cuandoLoDevuelvoYConsulto_entoncesNoHayPrestamosActivos() {

        Loan loan = new Loan(book1, user1, new Date(), Loan.LoanStatus.ACTIVE, null);
        when(loanRepository.findByUserIdAndBookIdAndStatus(1L, 1L, Loan.LoanStatus.ACTIVE.name()))
                .thenReturn(Optional.of(loan));
        when(loanRepository.save(any(Loan.class))).thenAnswer(i -> i.getArgument(0));
        when(loanRepository.findByUserId(1L)).thenReturn(List.of());


        loanService.returnLoan(1L, 1L);
        List<Loan> prestamosPostDevolucion = loanService.getLoansByUser(1L);


        assertTrue(prestamosPostDevolucion.isEmpty());
    }
}