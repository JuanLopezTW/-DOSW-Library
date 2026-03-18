package edu.eci.dosw.tdd.controller;

import edu.eci.dosw.tdd.controller.dto.BookDTO;
import edu.eci.dosw.tdd.controller.mapper.BookMapper;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.service.BookService;
import edu.eci.dosw.tdd.core.validator.BookValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private final BookValidator bookValidator;
    private final BookMapper bookMapper;

    public BookController(BookService bookService, BookValidator bookValidator, BookMapper bookMapper) {
        this.bookService = bookService;
        this.bookValidator = bookValidator;
        this.bookMapper = bookMapper;
    }

    @PostMapping
    public ResponseEntity<Void> addBook(@RequestBody BookDTO bookDTO) {
        Book book = bookMapper.toModel(bookDTO);
        bookValidator.validate(book);
        bookService.addBook(book, bookDTO.getCopies());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> books = bookService.getAllBooks().entrySet().stream()
                .map(entry -> bookMapper.toDTO(entry.getKey(), entry.getValue()))
                .toList();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBook(@PathVariable Long id) {
        Book book = bookService.getBook(id);
        Integer copies = bookService.getAllBooks().get(book);
        return ResponseEntity.ok(bookMapper.toDTO(book, copies));
    }
}
