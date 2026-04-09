package edu.eci.dosw.tdd.persistence.nonrelational.repository;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.repository.BookRepository;
import edu.eci.dosw.tdd.persistence.nonrelational.document.BookDocument;
import edu.eci.dosw.tdd.persistence.nonrelational.mapper.BookDocumentMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("mongo")
public class BookRepositoryMongoImpl implements BookRepository {

    private final MongoBookRepository repository;
    private final BookDocumentMapper mapper;

    public BookRepositoryMongoImpl(MongoBookRepository repository, BookDocumentMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Book save(Book book, int copies) {
        BookDocument doc = mapper.toDocument(book, copies);
        return mapper.toModel(repository.save(doc));
    }

    @Override
    public Optional<Book> findById(Long id) {
        return repository.findById(String.valueOf(id)).map(mapper::toModel);
    }

    @Override
    public List<Book> findAll() {
        return repository.findAll().stream().map(mapper::toModel).toList();
    }

    @Override
    public void decreaseCopy(Long id) {
        BookDocument doc = repository.findById(String.valueOf(id)).orElseThrow();
        doc.getAvailability().setAvailableCopies(doc.getAvailability().getAvailableCopies() - 1);
        doc.getAvailability().setLoanedCopies(doc.getAvailability().getLoanedCopies() + 1);
        repository.save(doc);
    }

    @Override
    public void increaseCopy(Long id) {
        BookDocument doc = repository.findById(String.valueOf(id)).orElseThrow();
        doc.getAvailability().setAvailableCopies(doc.getAvailability().getAvailableCopies() + 1);
        doc.getAvailability().setLoanedCopies(doc.getAvailability().getLoanedCopies() - 1);
        repository.save(doc);
    }

    @Override
    public void updateStock(Long id, int newTotalCopies) {
        BookDocument doc = repository.findById(String.valueOf(id)).orElseThrow();
        int diff = newTotalCopies - doc.getAvailability().getTotalCopies();
        doc.getAvailability().setTotalCopies(newTotalCopies);
        doc.getAvailability().setAvailableCopies(Math.max(0, doc.getAvailability().getAvailableCopies() + diff));
        repository.save(doc);
    }
}
