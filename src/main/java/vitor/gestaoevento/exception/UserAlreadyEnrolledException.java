package vitor.gestaoevento.exception;

public class UserAlreadyEnrolledException extends RuntimeException {
    public UserAlreadyEnrolledException(String message) {
        super(message);
    }
}
