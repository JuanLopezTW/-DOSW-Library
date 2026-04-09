package edu.eci.dosw.tdd.persistence.relational.repository;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.repository.BookRepository;
import edu.eci.dosw.tdd.persistence.relational.entity.BookEntity;
import edu.eci.dosw.tdd.persistence.relational.mapper.BookPersistenceMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("relational")
public class BookRepositoryJpaImpl implements BookRepository {

    private final JpaBookRepository repository;
    private final BookPersistenceMapper mapper;

    public BookRepositoryJpaImpl(JpaBookRepository repository, BookPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Book save(Book book, int copies) {
        BookEntity entity = mapper.toEntity(book, copies);
        return mapper.toModel(repository.save(entity));
    }

    @Override
    public Optional<Book> findById(Long id) {
        return repository.findById(id).map(mapper::toModel);
    }

    @Override
    public List<Book> findAll() {
        return repository.findAll().stream().map(mapper::toModel).toList();
    }

    @Override
    public void decreaseCopy(Long id) {
        BookEntity entity = repository.findById(id).orElseThrow();
        entity.setAvailableCopies(entity.getAvailableCopies() - 1);
        repository.save(entity);
    }

    @Override
    public void increaseCopy(Long id) {
        BookEntity entity = repository.findById(id).orElseThrow();
        entity.setAvailableCopies(entity.getAvailableCopies() + 1);
        repository.save(entity);
    }

    @Override
    public void updateStock(Long id, int newTotalCopies) {
        BookEntity entity = repository.findById(id).orElseThrow();
        int diff = newTotalCopies - entity.getTotalCopies();
        entity.setTotalCopies(newTotalCopies);
        entity.setAvailableCopies(Math.max(0, entity.getAvailableCopies() + diff));
        repository.save(entity);
    }
}
