package pl.jakubholik90.infrastructure.exception;

public class EmailSyncException extends RuntimeException {
    public EmailSyncException(String message) {
        super(message);
    }
}
