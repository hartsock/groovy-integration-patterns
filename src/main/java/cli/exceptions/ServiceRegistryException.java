package cli.exceptions;

/**
 * @author Shawn Hartsock
 */
public class ServiceRegistryException extends CliException {
    public ServiceRegistryException(String message) {
        super(message);
    }

    public ServiceRegistryException(Throwable t) {
        super(t);
    }
}
