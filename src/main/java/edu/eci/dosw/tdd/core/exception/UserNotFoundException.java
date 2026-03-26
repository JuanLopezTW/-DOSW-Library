package edu.eci.dosw.tdd.core.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("El usuario con la id " + id + " no se ha encontrado.");
    }
}
