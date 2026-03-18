package edu.eci.dosw.tdd.core.model;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
public class Loan {
    private Book book;
    private User user;
    private Date loanDate;
    private String status;
    private Date returnDate;

    public Loan(Book book,User user, Date loanDate,String status, Date returnDate){
        this.book = book;
        this.user = user;
        this.loanDate = loanDate;
        this.status = status;
        this.returnDate = returnDate;
    }
}
