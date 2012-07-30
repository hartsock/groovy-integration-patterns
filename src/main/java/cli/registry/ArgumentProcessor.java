package cli.registry;

/**
 * @author Shawn Hartsock
 */
public interface ArgumentProcessor {
    void injectArguments(final Object command, final String[] arguments);
}
