package cli.exceptions;

import java.lang.reflect.Method;

/**
 * @author Shawn Hartsock
 */
public class CommandSyntaxErrorException extends CliException {
    public CommandSyntaxErrorException(Method method) {
        super("Check " + method.getDeclaringClass().getCanonicalName() + "." + method.getName() + " is public.");
    }
}
