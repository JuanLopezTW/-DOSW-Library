package edu.eci.dosw.tdd.persistence.nonrelational.document;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "loans")
public class LoanDocument {

    @Id
    private String id;
    private String userId;
    private String bookId;
    private Date loanDate;
    private Date returnDate;
    private String status;
    private List<LoanHistory> history;

    @Data
    @NoArgsConstructor
    public static class LoanHistory {
        private String status;
        private Date executedAt;
    }
}