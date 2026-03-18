package edu.eci.dosw.tdd.core.exception;

public class LoanLimitExeededException extends RuntimeException{
    public LoanLimitExeededException(Long Id) {
        super("El usuario de id " + Id + "ha exedido el limite de su prestamo");
    }
}
