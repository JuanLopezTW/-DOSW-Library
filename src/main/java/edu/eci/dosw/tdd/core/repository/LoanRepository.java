package edu.eci.dosw.tdd.core.repository;


import edu.eci.dosw.tdd.core.model.Loan;
import java.util.List;
import java.util.Optional;

public interface LoanRepository {
    Loan save(Loan loan);
    List<Loan> findAll();
    List<Loan> findByUserId(Long userId);
    List<Loan> findByUserIdAndStatus(Long userId, String status);
    Optional<Loan> findByUserIdAndBookIdAndStatus(Long userId, Long bookId, String status);
}