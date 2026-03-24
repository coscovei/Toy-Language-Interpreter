package exception;

public class ADTException extends RuntimeException {
    public ADTException(String message) {
        super("ADT Exception: " + message);
    }
}
