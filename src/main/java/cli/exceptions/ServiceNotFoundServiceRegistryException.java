package cli.exceptions;

/**
 * @author Shawn Hartsock
 */
public class ServiceNotFoundServiceRegistryException extends ServiceRegistryException {
    public ServiceNotFoundServiceRegistryException(String message) {
        super(message);
    }

    public ServiceNotFoundServiceRegistryException(Throwable t) {
        super(t);
    }
}
