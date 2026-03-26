package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.exception.LoanLimitExeededException;
import edu.eci.dosw.tdd.core.exception.LoanNotFoundException;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.util.DateUtil;
import edu.eci.dosw.tdd.persistence.entity.BookEntity;
import edu.eci.dosw.tdd.persistence.entity.LoanEntity;
import edu.eci.dosw.tdd.persistence.entity.UserEntity;
import edu.eci.dosw.tdd.persistence.mapper.LoanPersistenceMapper;
import edu.eci.dosw.tdd.persistence.repository.BookRepository;
import edu.eci.dosw.tdd.persistence.repository.LoanRepository;
import edu.eci.dosw.tdd.persistence.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanService {

    private static final int MAX_LOANS = 3;

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final LoanPersistenceMapper loanMapper;
    private final BookService bookService;
    private final UserService userService;

    public LoanService(LoanRepository loanRepository, BookRepository bookRepository,UserRepository userRepository, LoanPersistenceMapper loanMapper,
                       BookService bookService, UserService userService) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.loanMapper = loanMapper;
        this.bookService = bookService;
        this.userService = userService;
    }

    public Loan createLoan(Long userId, Long bookId) {
        User user = userService.getUser(userId);
        Book book = bookService.getBook(bookId);

        long activeLoans = loanRepository.findByUserIdAndStatus(userId, Loan.LoanStatus.ACTIVE.name()).size();
        if (activeLoans >= MAX_LOANS) throw new LoanLimitExeededException(userId);

        bookService.decreaseCopy(bookId);

        UserEntity userEntity = userRepository.findById(userId).orElseThrow();
        BookEntity bookEntity = bookRepository.findById(bookId).orElseThrow();

        LoanEntity entity = new LoanEntity();
        entity.setUser(userEntity);
        entity.setBook(bookEntity);
        entity.setLoanDate(DateUtil.today());
        entity.setStatus(Loan.LoanStatus.ACTIVE.name());
        loanRepository.save(entity);

        return loanMapper.toModel(entity);
    }

    public void returnLoan(Long userId, Long bookId) {
        LoanEntity entity = loanRepository
                .findByUserIdAndBookIdAndStatus(userId, bookId, Loan.LoanStatus.ACTIVE.name())
                .orElseThrow(() -> new LoanNotFoundException());

        entity.setStatus(Loan.LoanStatus.RETURNED.name());
        entity.setReturnDate(DateUtil.today());
        loanRepository.save(entity);
        bookService.increaseCopy(bookId);
    }

    public List<Loan> getAllLoans() {
        return loanRepository.findAll().stream()
                .map(loanMapper::toModel)
                .toList();
    }

    public List<Loan> getLoansByUser(Long userId) {
        return loanRepository.findByUserId(userId).stream()
                .map(loanMapper::toModel)
                .toList();
    }
}