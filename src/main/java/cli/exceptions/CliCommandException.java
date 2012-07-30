package cli.exceptions;

import java.lang.reflect.Method;

/**
 * @author Shawn Hartsock
 */
public class CliCommandException extends CliException {
    public CliCommandException(String name, Method method, Throwable cause) {
        super("Command: " + name + " method: " + method.getName(),cause);
    }
}
