package edu.eci.dosw.tdd.core.exception;

public class LoanNotFoundException extends RuntimeException{
    public LoanNotFoundException(){
        super("Préstamo no encontrado");
    }
}
