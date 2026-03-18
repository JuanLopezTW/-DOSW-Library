package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookServiceTest {

    private BookService bookService;
    private Book book1;
    private Book book2;

    @BeforeEach
    void setUp() {
        bookService = new BookService();
        book1 = new Book("El Principito", "Antoine", 1L);
        book2 = new Book("Cien Anos de Soledad", "Garcia Marquez", 2L);
    }

    @Test
    void testAddBookSuccessfully() {
        bookService.addBook(book1, 3);
        assertEquals(1, bookService.getAllBooks().size());
    }

    @Test
    void testAddBookIncreasesExistingCopies() {
        bookService.addBook(book1, 3);
        bookService.addBook(book1, 2);
        assertEquals(5, bookService.getAllBooks().get(book1));
    }

    @Test
    void testGetBookSuccessfully() {
        bookService.addBook(book1, 3);
        Book found = bookService.getBook(1L);
        assertEquals(book1, found);
    }

    @Test
    void testDecreaseCopySuccessfully() {
        bookService.addBook(book1, 3);
        bookService.decreaseCopy(1L);
        assertEquals(2, bookService.getAllBooks().get(book1));
    }

    @Test
    void testIncreaseCopySuccessfully() {
        bookService.addBook(book1, 3);
        bookService.increaseCopy(1L);
        assertEquals(4, bookService.getAllBooks().get(book1));
    }

    @Test
    void testGetAllBooks() {
        bookService.addBook(book1, 3);
        bookService.addBook(book2, 5);
        assertEquals(2, bookService.getAllBooks().size());
    }

    @Test
    void testGetBookNotFound() {
        assertThrows(BookNotAvailableException.class, () -> bookService.getBook(99L));
    }

    @Test
    void testDecreaseCopyBookNotFound() {
        assertThrows(BookNotAvailableException.class, () -> bookService.decreaseCopy(99L));
    }

    @Test
    void testDecreaseCopyNoAvailableCopies() {
        bookService.addBook(book1, 1);
        bookService.decreaseCopy(1L);
        assertThrows(BookNotAvailableException.class, () -> bookService.decreaseCopy(1L));
    }
}
