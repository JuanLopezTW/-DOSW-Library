package edu.eci.dosw.tdd.persistence.mapper;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.persistence.entity.BookEntity;
import org.springframework.stereotype.Component;

@Component
public class BookPersistenceMapper {

    public BookEntity toEntity(Book book, int availableCopies) {
        BookEntity entity = new BookEntity();
        entity.setTitle(book.getTitle());
        entity.setAuthor(book.getAuthor());
        entity.setTotalCopies(availableCopies);
        entity.setAvailableCopies(availableCopies);
        return entity;
    }

    public Book toModel(BookEntity entity) {
        return new Book(entity.getTitle(), entity.getAuthor(), entity.getId(), entity.getAvailableCopies());
    }
}
