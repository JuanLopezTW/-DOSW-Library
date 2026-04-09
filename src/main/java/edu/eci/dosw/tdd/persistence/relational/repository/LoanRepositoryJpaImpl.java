package edu.eci.dosw.tdd.persistence.relational.repository;

import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.repository.LoanRepository;
import edu.eci.dosw.tdd.persistence.relational.mapper.LoanPersistenceMapper;
import edu.eci.dosw.tdd.persistence.relational.entity.BookEntity;
import edu.eci.dosw.tdd.persistence.relational.entity.UserEntity;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("relational")
public class LoanRepositoryJpaImpl implements LoanRepository {

    private final JpaLoanRepository repository;
    private final LoanPersistenceMapper mapper;
    private final JpaBookRepository bookRepository;
    private final JpaUserRepository userRepository;

    public LoanRepositoryJpaImpl(JpaLoanRepository repository, LoanPersistenceMapper mapper,
                                 JpaBookRepository bookRepository, JpaUserRepository userRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Loan save(Loan loan) {
        UserEntity userEntity = userRepository.findById(loan.getUser().getId()).orElseThrow();
        BookEntity bookEntity = bookRepository.findById(loan.getBook().getId()).orElseThrow();
        return mapper.toModel(repository.save(mapper.toEntity(loan, userEntity, bookEntity)));
    }

    @Override
    public List<Loan> findAll() {
        return repository.findAll().stream().map(mapper::toModel).toList();
    }

    @Override
    public List<Loan> findByUserId(Long userId) {
        return repository.findByUserId(userId).stream().map(mapper::toModel).toList();
    }

    @Override
    public List<Loan> findByUserIdAndStatus(Long userId, String status) {
        return repository.findByUserIdAndStatus(userId, status).stream().map(mapper::toModel).toList();
    }

    @Override
    public Optional<Loan> findByUserIdAndBookIdAndStatus(Long userId, Long bookId, String status) {
        return repository.findByUserIdAndBookIdAndStatus(userId, bookId, status).map(mapper::toModel);
    }
}