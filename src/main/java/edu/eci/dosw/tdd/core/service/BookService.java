package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.util.IdGeneratorUtil;
import edu.eci.dosw.tdd.core.util.ValidationUtil;
import edu.eci.dosw.tdd.persistence.entity.BookEntity;
import edu.eci.dosw.tdd.persistence.mapper.BookPersistenceMapper;
import edu.eci.dosw.tdd.persistence.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookPersistenceMapper bookMapper;

    public BookService(BookRepository bookRepository, BookPersistenceMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    public void addBook(Book book, int copies) {
        if (!ValidationUtil.isPositive(copies)) throw new IllegalArgumentException("Copies must be positive");
        if (book.getId() == null) book.setId(IdGeneratorUtil.generateNumericId());
        BookEntity entity = bookMapper.toEntity(book, copies);
        bookRepository.save(entity);
    }

    public void updateStock(Long id, int newTotalCopies) {
        if (!ValidationUtil.isPositive(newTotalCopies))
            throw new IllegalArgumentException("El stock total debe ser mayor a 0");
        BookEntity entity = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotAvailableException(id));
        int diff = newTotalCopies - entity.getTotalCopies();
        entity.setTotalCopies(newTotalCopies);
        entity.setAvailableCopies(Math.max(0, entity.getAvailableCopies() + diff));
        bookRepository.save(entity);
    }

    public Book getBook(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toModel)
                .orElseThrow(() -> new BookNotAvailableException(id));
    }

    public void decreaseCopy(Long id) {
        BookEntity entity = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotAvailableException(id));
        if (entity.getAvailableCopies() <= 0) throw new BookNotAvailableException(id);
        entity.setAvailableCopies(entity.getAvailableCopies() - 1);
        bookRepository.save(entity);
    }

    public void increaseCopy(Long id) {
        BookEntity entity = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotAvailableException(id));
        if (entity.getAvailableCopies() >= entity.getTotalCopies()) {
            throw new IllegalArgumentException("No se pueden devolver más copias que el total del libro");
        }
        entity.setAvailableCopies(entity.getAvailableCopies() + 1);
        bookRepository.save(entity);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toModel)
                .toList();
    }
}
