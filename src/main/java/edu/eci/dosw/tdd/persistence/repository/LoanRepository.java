package edu.eci.dosw.tdd.persistence.repository;

import edu.eci.dosw.tdd.persistence.entity.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<LoanEntity, Long> {
    List<LoanEntity> findByUserId(Long userId);
    List<LoanEntity> findByUserIdAndStatus(Long userId, String status);
    Optional<LoanEntity> findByUserIdAndBookIdAndStatus(Long userId, Long bookId, String status);
}
