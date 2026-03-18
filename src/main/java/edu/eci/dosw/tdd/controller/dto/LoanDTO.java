package edu.eci.dosw.tdd.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanDTO {
    private Long bookId;
    private String bookTitle;
    private Long userId;
    private String userName;
    private Date loanDate;
    private String status;
    private Date returnDate;
}
