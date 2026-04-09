package edu.eci.dosw.tdd.persistence.nonrelational.document;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "books")
public class BookDocument {

    @Id
    private String id;
    private String title;
    private String author;
    private List<String> categories;
    private String publicationType;
    private Date publishedDate;
    private String isbn;
    private Date addedAt;
    private Metadata metadata;
    private Availability availability;

    @Data
    @NoArgsConstructor
    public static class Metadata {
        private Integer pages;
        private String language;
        private String publisher;
    }

    @Data
    @NoArgsConstructor
    public static class Availability {
        private String status;
        private Integer totalCopies;
        private Integer availableCopies;
        private Integer loanedCopies;
    }
}
