package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.repository.BookRepository;
import edu.eci.dosw.tdd.core.util.IdGeneratorUtil;
import edu.eci.dosw.tdd.core.util.ValidationUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void addBook(Book book, int copies) {
        if (!ValidationUtil.isPositive(copies)) throw new IllegalArgumentException("Copies must be positive");
        if (book.getId() == null) book.setId(IdGeneratorUtil.generateNumericId());
        bookRepository.save(book, copies);
    }

    public void updateStock(Long id, int newTotalCopies) {
        if (!ValidationUtil.isPositive(newTotalCopies))
            throw new IllegalArgumentException("El stock total debe ser mayor a 0");
        bookRepository.updateStock(id, newTotalCopies);
    }

    public Book getBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotAvailableException(id));
    }

    public void decreaseCopy(Long id) {
        bookRepository.findById(id).orElseThrow(() -> new BookNotAvailableException(id));
        bookRepository.decreaseCopy(id);
    }

    public void increaseCopy(Long id) {
        bookRepository.findById(id).orElseThrow(() -> new BookNotAvailableException(id));
        bookRepository.increaseCopy(id);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
}
