package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.persistence.entity.BookEntity;
import edu.eci.dosw.tdd.persistence.mapper.BookPersistenceMapper;
import edu.eci.dosw.tdd.persistence.repository.BookRepository;
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
    private BookPersistenceMapper bookMapper;
    private Book book1;
    private BookEntity bookEntity1;

    @BeforeEach
    void setUp() {
        bookRepository = Mockito.mock(BookRepository.class);
        bookMapper = Mockito.mock(BookPersistenceMapper.class);
        bookService = new BookService(bookRepository, bookMapper);

        book1 = new Book("El Principito", "Antoine", 1L, 3);
        bookEntity1 = new BookEntity(1L, "El Principito", "Antoine", 3, 2);
    }

    @Test
    void testAddBookSuccessfully() {
        when(bookMapper.toEntity(book1, 3)).thenReturn(bookEntity1);
        bookService.addBook(book1, 3);
        verify(bookRepository, times(1)).save(bookEntity1);
    }

    @Test
    void testGetBookSuccessfully() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookEntity1));
        when(bookMapper.toModel(bookEntity1)).thenReturn(book1);
        Book found = bookService.getBook(1L);
        assertEquals(book1, found);
    }

    @Test
    void testGetAllBooks() {
        when(bookRepository.findAll()).thenReturn(List.of(bookEntity1));
        when(bookMapper.toModel(bookEntity1)).thenReturn(book1);
        assertEquals(1, bookService.getAllBooks().size());
    }

    @Test
    void testDecreaseCopySuccessfully() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookEntity1));
        bookService.decreaseCopy(1L);
        assertEquals(1, bookEntity1.getAvailableCopies());
    }


    @Test
    void testIncreaseCopySuccessfully() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookEntity1));
        bookService.increaseCopy(1L);
        assertEquals(3, bookEntity1.getAvailableCopies());
    }

    @Test
    void testGetBookNotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(BookNotAvailableException.class, () -> bookService.getBook(99L));
    }

    @Test
    void testUpdateStockSuccessfully() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookEntity1));
        bookService.updateStock(1L, 5);
        assertEquals(5, bookEntity1.getTotalCopies());
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
    void testDecreaseCopyNoAvailableCopies() {
        bookEntity1.setAvailableCopies(0);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookEntity1));
        assertThrows(BookNotAvailableException.class, () -> bookService.decreaseCopy(1L));
    }

    @Test
    void testAddBookInvalidCopies() {
        assertThrows(IllegalArgumentException.class, () -> bookService.addBook(book1, 0));
    }

    @Test
    void testIncreaseCopyExceedsTotalCopies() {
        bookEntity1 = new BookEntity(1L, "El Principito", "Antoine", 3, 3);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookEntity1));
        assertThrows(IllegalArgumentException.class, () -> bookService.increaseCopy(1L));
    }

}
