package cli.exceptions;

/**
 * @author Shawn Hartsock
 */
public class CommandInstantiationException extends CliException {
    public CommandInstantiationException(Throwable t) {
        super(t);
    }
}
