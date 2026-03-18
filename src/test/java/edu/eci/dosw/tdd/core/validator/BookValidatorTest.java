package edu.eci.dosw.tdd.core.validator;


import edu.eci.dosw.tdd.core.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookValidatorTest {

    private BookValidator bookValidator;

    @BeforeEach
    void setUp() {
        bookValidator = new BookValidator();
    }

    @Test
    void testValidateBookSuccessfully() {
        Book book = new Book("El Principito", "Antoine", 1L);
        assertDoesNotThrow(() -> bookValidator.validate(book));
    }

    @Test
    void testValidateBookNull() {
        assertThrows(IllegalArgumentException.class, () -> bookValidator.validate(null));
    }

    @Test
    void testValidateBookIdNull() {
        Book book = new Book("El Principito", "Antoine", null);
        assertThrows(IllegalArgumentException.class, () -> bookValidator.validate(book));
    }

    @Test
    void testValidateBookTitleEmpty() {
        Book book = new Book("", "Antoine", 1L);
        assertThrows(IllegalArgumentException.class, () -> bookValidator.validate(book));
    }

    @Test
    void testValidateBookAuthorEmpty() {
        Book book = new Book("El Principito", "", 1L);
        assertThrows(IllegalArgumentException.class, () -> bookValidator.validate(book));
    }
}
