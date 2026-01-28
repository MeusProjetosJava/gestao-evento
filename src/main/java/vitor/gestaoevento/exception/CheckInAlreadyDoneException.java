package vitor.gestaoevento.exception;

public class CheckInAlreadyDoneException extends RuntimeException {
    public CheckInAlreadyDoneException(String message) {
        super(message);
    }
}
