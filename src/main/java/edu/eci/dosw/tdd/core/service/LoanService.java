package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.exception.LoanLimitExeededException;
import edu.eci.dosw.tdd.core.exception.LoanNotFoundException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.repository.LoanRepository;
import edu.eci.dosw.tdd.core.util.DateUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanService {

    private static final int MAX_LOANS = 3;

    private final LoanRepository loanRepository;
    private final BookService bookService;
    private final UserService userService;

    public LoanService(LoanRepository loanRepository, BookService bookService, UserService userService) {
        this.loanRepository = loanRepository;
        this.bookService = bookService;
        this.userService = userService;
    }

    public Loan createLoan(Long userId, Long bookId) {
        User user = userService.getUser(userId);
        Book book = bookService.getBook(bookId);

        long activeLoans = loanRepository.findByUserIdAndStatus(userId, Loan.LoanStatus.ACTIVE.name()).size();
        if (activeLoans >= MAX_LOANS) throw new LoanLimitExeededException(userId);

        bookService.decreaseCopy(bookId);

        Loan loan = new Loan(book, user, DateUtil.today(), Loan.LoanStatus.ACTIVE, null);
        return loanRepository.save(loan);
    }

    public void returnLoan(Long userId, Long bookId) {
        Loan loan = loanRepository
                .findByUserIdAndBookIdAndStatus(userId, bookId, Loan.LoanStatus.ACTIVE.name())
                .orElseThrow(LoanNotFoundException::new);

        loan.setStatus(Loan.LoanStatus.RETURNED);
        loan.setReturnDate(DateUtil.today());
        loanRepository.save(loan);
        bookService.increaseCopy(bookId);
    }

    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    public List<Loan> getLoansByUserAndStatus(Long userId, String status) {
        return loanRepository.findByUserIdAndStatus(userId, status);
    }

    public List<Loan> getLoansByUser(Long userId) {
        return loanRepository.findByUserId(userId);
    }
}