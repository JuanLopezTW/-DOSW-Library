package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.util.ValidationUtil;
import edu.eci.dosw.tdd.core.util.IdGeneratorUtil;
import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.model.Book;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookService {
    private final Map<Book, Integer> books = new HashMap<>();

    public void addBook(Book book, int copies) {
        if (!ValidationUtil.isPositive(copies)) throw new IllegalArgumentException("Copies must be positive");
        if (book.getId() == null) book.setId(IdGeneratorUtil.generateNumericId());
        books.put(book, books.getOrDefault(book, 0) + copies);
    }

    public Book getBook(Long id) {
        Book book = books.keySet().stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new BookNotAvailableException(id));
        return book;
    }

    public void increaseCopy(Long id) {
        Book book = getBook(id);
        books.put(book, books.get(book) + 1);
    }

    public void decreaseCopy(Long id) {
        Book book = getBook(id);
        int copies = books.get(book);
        if (copies <= 0) throw new BookNotAvailableException(id);
        books.put(book, copies - 1);
    }

    public Map<Book, Integer> getAllBooks() {
        return books;
    }

}
