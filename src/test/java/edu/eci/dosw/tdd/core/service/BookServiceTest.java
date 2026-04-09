package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    private BookService bookService;
    private BookRepository bookRepository;
    private Book book1;

    @BeforeEach
    void setUp() {
        bookRepository = Mockito.mock(BookRepository.class);
        bookService = new BookService(bookRepository);
        book1 = new Book("El Principito", "Antoine", 1L, 3);
    }

    @Test
    void testAddBookSuccessfully() {
        bookService.addBook(book1, 3);
        verify(bookRepository, times(1)).save(book1, 3);
    }

    @Test
    void testGetBookSuccessfully() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        Book found = bookService.getBook(1L);
        assertEquals(book1, found);
    }

    @Test
    void testGetAllBooks() {
        when(bookRepository.findAll()).thenReturn(List.of(book1));
        assertEquals(1, bookService.getAllBooks().size());
    }

    @Test
    void testDecreaseCopySuccessfully() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        bookService.decreaseCopy(1L);
        verify(bookRepository, times(1)).decreaseCopy(1L);
    }

    @Test
    void testIncreaseCopySuccessfully() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        bookService.increaseCopy(1L);
        verify(bookRepository, times(1)).increaseCopy(1L);
    }

    @Test
    void testGetBookNotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(BookNotAvailableException.class, () -> bookService.getBook(99L));
    }

    @Test
    void testUpdateStockSuccessfully() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        bookService.updateStock(1L, 5);
        verify(bookRepository, times(1)).updateStock(1L, 5);
    }

    @Test
    void testUpdateStockInvalid() {
        assertThrows(IllegalArgumentException.class, () -> bookService.updateStock(1L, 0));
    }

    @Test
    void testDecreaseCopyBookNotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(BookNotAvailableException.class, () -> bookService.decreaseCopy(99L));
    }

    @Test
    void testAddBookInvalidCopies() {
        assertThrows(IllegalArgumentException.class, () -> bookService.addBook(book1, 0));
    }
}