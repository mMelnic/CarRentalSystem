package carrental.exceptions;

public class RentalIntervalException extends Exception {

    public RentalIntervalException(String message) {
        super(message);
    }

    public RentalIntervalException(String message, Throwable cause) {
        super(message, cause);
    }
}
