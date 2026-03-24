package exception;

public class ExpEvalException extends RuntimeException {
    public ExpEvalException(String message) {
        super("Expression Evaluation Exception: " + message);
    }
}
