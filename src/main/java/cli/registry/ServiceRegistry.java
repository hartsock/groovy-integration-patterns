package cli.registry;

import cli.exceptions.CliException;

/**
 * @author Shawn Hartsock
 */
public interface ServiceRegistry {
    void regiser(final String name, final Object service) throws CliException;
    Object lookup(final String name) throws CliException;
    void injectServices(final Object object);
}
