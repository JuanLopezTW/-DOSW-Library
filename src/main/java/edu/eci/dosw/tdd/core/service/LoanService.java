package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.util.DateUtil;
import edu.eci.dosw.tdd.core.exception.LoanLimitExeededException;
import edu.eci.dosw.tdd.core.exception.LoanNotFoundException;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.model.Book;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class LoanService {

    private static final int MAX_LOANS = 3;

    private final List<Loan> loans = new ArrayList<>();
    private final BookService bookService;
    private final UserService userService;

    public LoanService(BookService bookService, UserService userService) {
        this.bookService = bookService;
        this.userService = userService;
    }

    public Loan createLoan(Long userId, Long bookId) {
        User user = userService.getUser(userId);
        Book book = bookService.getBook(bookId);

        long activeLoans = loans.stream()
                .filter(l -> l.getUser().getId() == userId && l.getStatus().equals("ACTIVE"))
                .count();

        if (activeLoans >= MAX_LOANS) throw new LoanLimitExeededException(userId);

        bookService.decreaseCopy(bookId);

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setUser(user);
        loan.setLoanDate(DateUtil.today());
        loan.setStatus("ACTIVE");
        loans.add(loan);
        return loan;
    }

    public void returnLoan(Long userId, Long bookId) {
        Loan loan = loans.stream()
                .filter(l -> l.getUser().getId() == userId
                        && l.getBook().getId().equals(bookId)
                        && l.getStatus().equals("ACTIVE"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        loan.setStatus("RETURNED");
        loan.setReturnDate(DateUtil.today());
        bookService.increaseCopy(bookId);
    }

    public List<Loan> getAllLoans() {
        return loans;
    }

    public List<Loan> getLoansByUser(Long userId) {
        return loans.stream()
                .filter(l -> l.getUser().getId() == userId)
                .toList();
    }
}