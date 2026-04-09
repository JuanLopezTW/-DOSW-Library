package edu.eci.dosw.tdd.persistence.nonrelational.repository;

import edu.eci.dosw.tdd.persistence.nonrelational.document.BookDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoBookRepository extends MongoRepository<BookDocument, String> {
}