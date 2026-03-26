package edu.eci.dosw.tdd.core.model;

import lombok.*;


@Data
@NoArgsConstructor
public class Book {
    private Long id;
    private String title;
    private String author;
    private Integer availableCopies;

    public Book(String title, String author, Long id) {
        this.title = title;
        this.author = author;
        this.id = id;
    }

    public Book(String title, String author, Long id, Integer availableCopies) {
        this.title = title;
        this.author = author;
        this.id = id;
        this.availableCopies = availableCopies;
    }
}
