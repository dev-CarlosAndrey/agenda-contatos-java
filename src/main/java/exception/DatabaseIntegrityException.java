package exception;

public class DatabaseIntegrityException extends AgendaException{
    public DatabaseIntegrityException(String message) {
        super(message);
    }

    public DatabaseIntegrityException(String message, Throwable cause) {
        super(message, cause);
    }
}
