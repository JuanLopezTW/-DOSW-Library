package edu.eci.dosw.tdd.persistence.nonrelational.mapper;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.persistence.nonrelational.document.LoanDocument;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;

@Component
public class LoanDocumentMapper {

    public LoanDocument toDocument(Loan loan) {
        LoanDocument doc = new LoanDocument();
        doc.setUserId(String.valueOf(loan.getUser().getId()));
        doc.setBookId(String.valueOf(loan.getBook().getId()));
        doc.setLoanDate(loan.getLoanDate());
        doc.setReturnDate(loan.getReturnDate());
        doc.setStatus(loan.getStatus().name());

        LoanDocument.LoanHistory history = new LoanDocument.LoanHistory();
        history.setStatus(loan.getStatus().name());
        history.setExecutedAt(new Date());

        doc.setHistory(new ArrayList<>());
        doc.getHistory().add(history);

        return doc;
    }

    public Loan toModel(LoanDocument doc) {
        Book book = new Book(null, null,
                doc.getBookId() != null ? Long.parseLong(doc.getBookId()) : 0L);
        User user = new User(null,
                doc.getUserId() != null ? Long.parseLong(doc.getUserId()) : 0L);
        return new Loan(book, user, doc.getLoanDate(),
                Loan.LoanStatus.valueOf(doc.getStatus()), doc.getReturnDate());
    }
}
