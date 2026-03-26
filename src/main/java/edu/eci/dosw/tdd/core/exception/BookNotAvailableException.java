package edu.eci.dosw.tdd.core.exception;

public class BookNotAvailableException extends RuntimeException{
    public BookNotAvailableException(Long bookId) {
        super("El libro con el id" + bookId + " no esta disponible ");
    }
}
