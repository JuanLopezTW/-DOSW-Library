package edu.eci.dosw.tdd.persistence.nonrelational.repository;

import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.repository.LoanRepository;
import edu.eci.dosw.tdd.persistence.nonrelational.mapper.LoanDocumentMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("mongo")
public class LoanRepositoryMongoImpl implements LoanRepository {

    private final MongoLoanRepository repository;
    private final LoanDocumentMapper mapper;

    public LoanRepositoryMongoImpl(MongoLoanRepository repository, LoanDocumentMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Loan save(Loan loan) {
        return mapper.toModel(repository.save(mapper.toDocument(loan)));
    }

    @Override
    public List<Loan> findAll() {
        return repository.findAll().stream().map(mapper::toModel).toList();
    }

    @Override
    public List<Loan> findByUserId(Long userId) {
        return repository.findByUserId(String.valueOf(userId)).stream().map(mapper::toModel).toList();
    }

    @Override
    public List<Loan> findByUserIdAndStatus(Long userId, String status) {
        return repository.findByUserIdAndStatus(String.valueOf(userId), status).stream().map(mapper::toModel).toList();
    }

    @Override
    public Optional<Loan> findByUserIdAndBookIdAndStatus(Long userId, Long bookId, String status) {
        return repository.findByUserIdAndBookIdAndStatus(
                String.valueOf(userId), String.valueOf(bookId), status).map(mapper::toModel);
    }
}
