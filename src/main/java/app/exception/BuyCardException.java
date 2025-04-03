package app.exception;

public class BuyCardException extends Exception {
    private static final long serialVersionUID = 1L;

    public BuyCardException() {
        super();
    }

    public BuyCardException(String message) {
        super(message);
    }

    public BuyCardException(String message, Throwable cause) {
        super(message, cause);
    }

    public BuyCardException(Throwable cause) {
        super(cause);
    }
}
