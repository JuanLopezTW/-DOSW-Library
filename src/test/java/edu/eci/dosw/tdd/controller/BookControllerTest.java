package edu.eci.dosw.tdd.controller;

import edu.eci.dosw.tdd.controller.dto.BookDTO;
import edu.eci.dosw.tdd.controller.mapper.BookMapper;
import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.service.BookService;
import edu.eci.dosw.tdd.core.validator.BookValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookControllerTest {

    private BookController bookController;
    private BookService bookService;
    private BookValidator bookValidator;
    private BookMapper bookMapper;

    @BeforeEach
    void setUp() {
        bookService = Mockito.mock(BookService.class);
        bookValidator = Mockito.mock(BookValidator.class);
        bookMapper = Mockito.mock(BookMapper.class);
        bookController = new BookController(bookService, bookValidator, bookMapper);
    }

    @Test
    void testGetAllBooksSuccessfully() {
        when(bookService.getAllBooks()).thenReturn(List.of());
        ResponseEntity<List<BookDTO>> response = bookController.getAllBooks();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testAddBookSuccessfully() {
        BookDTO bookDTO = new BookDTO(1L, "El Principito", "Antoine", 3);
        Book book = new Book("El Principito", "Antoine", 1L, 3);
        when(bookMapper.toModel(bookDTO)).thenReturn(book);
        ResponseEntity<Void> response = bookController.addBook(bookDTO);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testGetBookSuccessfully() {
        Book book = new Book("El Principito", "Antoine", 1L, 3);
        BookDTO bookDTO = new BookDTO(1L, "El Principito", "Antoine", 3);
        when(bookService.getBook(1L)).thenReturn(book);
        when(bookMapper.toDTO(book)).thenReturn(bookDTO);
        ResponseEntity<BookDTO> response = bookController.getBook(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetBookNotFound() {
        when(bookService.getBook(99L)).thenThrow(new BookNotAvailableException(99L));
        assertThrows(BookNotAvailableException.class, () -> bookController.getBook(99L));
    }

    @Test
    void testAddBookValidationFails() {
        BookDTO bookDTO = new BookDTO(1L, "", "Antoine", 3);
        Book book = new Book("", "Antoine", 1L, 3);
        when(bookMapper.toModel(bookDTO)).thenReturn(book);
        doThrow(new IllegalArgumentException("Book title cannot be empty"))
                .when(bookValidator).validate(book);
        assertThrows(IllegalArgumentException.class, () -> bookController.addBook(bookDTO));
    }
}