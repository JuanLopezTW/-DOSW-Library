package edu.eci.dosw.tdd.core.model;

import lombok.Data;

import java.util.Date;

@Data
public class Loan {
    private Book book;
    private User user;
    private Date loanDate;
    private String status;
    private Date returnDate;
}
