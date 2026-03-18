package edu.eci.dosw.tdd.core.validator;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoanValidatorTest {

    private LoanValidator loanValidator;
    private Book book1;
    private User user1;

    @BeforeEach
    void setUp() {
        loanValidator = new LoanValidator();
        book1 = new Book("El Principito", "Antoine", 1L);
        user1 = new User("Juan", 1L);
    }

    @Test
    void testValidateLoanSuccessfully() {
        Loan loan = new Loan(book1, user1, null, "ACTIVE", null);
        assertDoesNotThrow(() -> loanValidator.validate(loan));
    }

    @Test
    void testValidateLoanNull() {
        assertThrows(IllegalArgumentException.class, () -> loanValidator.validate(null));
    }

    @Test
    void testValidateLoanBookNull() {
        Loan loan = new Loan(null, user1, null, "ACTIVE", null);
        assertThrows(IllegalArgumentException.class, () -> loanValidator.validate(loan));
    }

    @Test
    void testValidateLoanUserNull() {
        Loan loan = new Loan(book1, null, null, "ACTIVE", null);
        assertThrows(IllegalArgumentException.class, () -> loanValidator.validate(loan));
    }
}
