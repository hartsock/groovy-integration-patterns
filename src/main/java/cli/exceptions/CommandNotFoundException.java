package cli.exceptions;

/**
 * @author Shawn Hartsock
 */
public class CommandNotFoundException extends CliException {
    public CommandNotFoundException(String commandName) {
        super("The command '" + commandName + "' was not found.");
    }
}
