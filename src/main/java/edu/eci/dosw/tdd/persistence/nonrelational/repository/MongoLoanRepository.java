package edu.eci.dosw.tdd.persistence.nonrelational.repository;

import edu.eci.dosw.tdd.persistence.nonrelational.document.LoanDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MongoLoanRepository extends MongoRepository<LoanDocument, String> {
    List<LoanDocument> findByUserId(String userId);
    List<LoanDocument> findByUserIdAndStatus(String userId, String status);
    Optional<LoanDocument> findByUserIdAndBookIdAndStatus(String userId, String bookId, String status);
}