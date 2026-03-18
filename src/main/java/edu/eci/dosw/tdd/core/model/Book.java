package edu.eci.dosw.tdd.core.model;

import lombok.*;

@Data
@NoArgsConstructor
public class Book {
    private String title;
    private String author;
    private Long id;

    public Book(String title, String author, Long id){
        this.title = title;
        this.author  = author ;
        this.id = id;
    }
}
