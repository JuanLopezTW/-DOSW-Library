package edu.eci.dosw.tdd.core.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
public class Loan {

    public enum LoanStatus {
        ACTIVE, RETURNED
    }

    private Book book;
    private User user;
    private Date loanDate;
    private LoanStatus status;
    private Date returnDate;

    public Loan(Book book, User user, Date loanDate, LoanStatus status, Date returnDate) {
        this.book = book;
        this.user = user;
        this.loanDate = loanDate;
        this.status = status;
        this.returnDate = returnDate;
    }
}
