package edu.eci.dosw.tdd.persistence.nonrelational.mapper;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.persistence.nonrelational.document.BookDocument;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class BookDocumentMapper {

    public BookDocument toDocument(Book book, int copies) {
        BookDocument doc = new BookDocument();
        doc.setId(String.valueOf(book.getId()));
        doc.setTitle(book.getTitle());
        doc.setAuthor(book.getAuthor());
        doc.setAddedAt(new Date());

        BookDocument.Availability availability = new BookDocument.Availability();
        availability.setStatus("AVAILABLE");
        availability.setTotalCopies(copies);
        availability.setAvailableCopies(copies);
        availability.setLoanedCopies(0);
        doc.setAvailability(availability);

        return doc;
    }

    public Book toModel(BookDocument doc) {
        return new Book(doc.getTitle(), doc.getAuthor(),
                doc.getId() != null ? Long.parseLong(doc.getId()) : null,
                doc.getAvailability() != null ? doc.getAvailability().getAvailableCopies() : 0);
    }
}