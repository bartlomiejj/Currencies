package currencies.domain.exceptions;

public class BadRateException extends AbstractException {

    public BadRateException(String message) {
        super(message);
    }
}
