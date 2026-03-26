package edu.eci.dosw.tdd.core.validator;

import edu.eci.dosw.tdd.core.model.Loan;
import org.springframework.stereotype.Component;

@Component
public class LoanValidator {

    public void validate(Loan loan) {
        if (loan == null) throw new IllegalArgumentException("Un prestamo no puede ser null");
        if (loan.getBook() == null) throw new IllegalArgumentException("Un prestamo no puede ser de un libro null");
        if (loan.getUser() == null) throw new IllegalArgumentException("El usuario del prestamo no puede ser null");
    }
}
