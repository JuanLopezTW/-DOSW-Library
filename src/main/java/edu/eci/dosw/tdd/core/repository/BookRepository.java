package edu.eci.dosw.tdd.core.repository;

import edu.eci.dosw.tdd.core.model.Book;
import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Book save(Book book, int copies);
    Optional<Book> findById(Long id);
    List<Book> findAll();
    void decreaseCopy(Long id);
    void increaseCopy(Long id);
    void updateStock(Long id, int newTotalCopies);
}
