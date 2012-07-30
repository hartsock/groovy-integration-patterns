package cli.registry;

import cli.annotations.Command;

import java.lang.reflect.Method;

/**
 * @author Shawn Hartsock
 */
public class CommandReference {
    final Command info;
    final Class<?> commandClass;

    final Method before;
    final Method run;
    final Method after;

    public CommandReference(Class<?> commandClass, Method before, Method run, Method after) {
        this.info = (Command) commandClass.getAnnotation(Command.class);
        this.commandClass = commandClass;
        this.before = before;
        this.run = run;
        this.after = after;
    }
}
