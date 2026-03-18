package edu.eci.dosw.tdd.controller.mapper;

import edu.eci.dosw.tdd.controller.dto.BookDTO;
import edu.eci.dosw.tdd.core.model.Book;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BookMapper {

    public BookDTO toDTO(Book book, Integer copies) {
        return new BookDTO(book.getId(), book.getTitle(), book.getAuthor(), copies);
    }

    public Book toModel(BookDTO dto) {
        return new Book(dto.getTitle(), dto.getAuthor(), dto.getId());
    }
}