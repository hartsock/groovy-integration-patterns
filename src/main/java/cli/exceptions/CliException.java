package cli.exceptions;

/**
 * @author Shawn Hartsock
 */
public class CliException extends RuntimeException {
    public CliException(String message) {
        super(message);
    }
    public CliException(Throwable t) {
        super(t);
    }
    public CliException(String message, Throwable cause) {
        super(message,cause);
    }
}
