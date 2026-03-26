package edu.eci.dosw.tdd.core.validator;

import edu.eci.dosw.tdd.core.model.Book;
import org.springframework.stereotype.Component;

@Component
public class BookValidator {

    public void validate(Book book) {
        if (book == null) throw new IllegalArgumentException("Un libro no puede ser null");
        if (book.getTitle() == null || book.getTitle().isBlank()) throw new IllegalArgumentException("El titulo de un libro no puede estar vacio");
        if (book.getAuthor() == null || book.getAuthor().isBlank()) throw new IllegalArgumentException("El libro debe tener un autor");
    }
}
