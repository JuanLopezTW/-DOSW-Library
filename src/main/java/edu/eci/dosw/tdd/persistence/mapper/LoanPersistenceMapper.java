package edu.eci.dosw.tdd.persistence.mapper;

import edu.eci.dosw.tdd.core.model.*;
import edu.eci.dosw.tdd.persistence.entity.BookEntity;
import edu.eci.dosw.tdd.persistence.entity.LoanEntity;
import edu.eci.dosw.tdd.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class LoanPersistenceMapper {

    public LoanEntity toEntity(Loan loan, UserEntity userEntity, BookEntity bookEntity) {
        LoanEntity entity = new LoanEntity();
        entity.setUser(userEntity);
        entity.setBook(bookEntity);
        entity.setLoanDate(loan.getLoanDate());
        entity.setReturnDate(loan.getReturnDate());
        entity.setStatus(loan.getStatus().name());
        return entity;
    }

    public Loan toModel(LoanEntity entity) {
        Book book = new Book(entity.getBook().getTitle(), entity.getBook().getAuthor(), entity.getBook().getId());
        User user = new User(entity.getUser().getName(), entity.getUser().getId());
        return new Loan(book, user, entity.getLoanDate(), Loan.LoanStatus.valueOf(entity.getStatus()), entity.getReturnDate());
    }
}
