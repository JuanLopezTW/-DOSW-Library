package edu.eci.dosw.tdd.controller.mapper;

import edu.eci.dosw.tdd.controller.dto.LoanDTO;
import edu.eci.dosw.tdd.core.model.Loan;
import org.springframework.stereotype.Component;

@Component
public class LoanMapper {
    public LoanDTO toDTO(Loan loan) {
        return new LoanDTO(
                loan.getBook().getId(),
                loan.getBook().getTitle(),
                loan.getUser().getId(),
                loan.getUser().getName(),
                loan.getLoanDate(),
                loan.getStatus(),
                loan.getReturnDate()
        );
    }
}