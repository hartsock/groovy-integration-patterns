package cli.registry;

import cli.annotations.After;
import cli.annotations.Before;
import cli.annotations.Command;
import cli.annotations.Run;
import cli.exceptions.CliCommandException;
import cli.exceptions.CommandInstantiationException;
import cli.exceptions.CommandNotFoundException;
import cli.exceptions.CommandSyntaxErrorException;
import com.beust.jcommander.JCommander;
import org.codehaus.groovy.runtime.ArrayUtil;
import org.codehaus.groovy.tools.shell.CommandException;
import org.scannotation.AnnotationDB;
import org.scannotation.ClasspathUrlFinder;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * @author Shawn Hartsock
 */
public class CommandRegistry {
    final Map<String,CommandReference> registry;
    final ServiceRegistry serviceRegistry;
    final ArgumentProcessor argumentProcessor;
    final ScriptLoader scriptLoader;

    public CommandRegistry(
            final Map<String,CommandReference> registry,
            final ServiceRegistry serviceRegistry,
            final ArgumentProcessor argumentProcessor,
            final ScriptLoader scriptLoader) {
        this.registry = registry;
        this.serviceRegistry = serviceRegistry;
        this.argumentProcessor = argumentProcessor;
        this.scriptLoader = scriptLoader;
    }

    @PostConstruct
    public void init() throws IOException, ClassNotFoundException {
        final AnnotationDB db = new AnnotationDB();
        final URL[] urls = ClasspathUrlFinder.findClassPaths();
        db.scanArchives(urls);
        final Map<String, Set<String>> annotationIndex = db.getAnnotationIndex();
        final Set<String> commandClasses = annotationIndex.get(Command.class.getName());
        for(String commandClass : commandClasses) {
            register(commandClass);
        }
    }

    public CommandReference get(final String name) {
        return registry.get(name);
    }

    public CommandReference register(String commandClass) throws ClassNotFoundException {
        final Class clazz = getClass(commandClass);
        return register(clazz);
    }

    public CommandReference register(Class clazz) {
        final CommandReference reference = commandReference(clazz);
        register(reference);
        return reference;
    }

    public static CommandReference commandReference(Class clazz) {
        final Method before = getMethod(clazz,Before.class);
        final Method run = getMethod(clazz,Run.class);
        final Method after = getMethod(clazz,After.class);
        return new CommandReference(clazz,before,run,after);
    }

    public void register(CommandReference reference) {
        registry.put(reference.info.name(),reference);
    }

    public static Method getMethod(Class<?> clazz, Class<? extends Annotation> annotation) {
        Method found = null;
        for(Method method : clazz.getMethods()) {
            if(found != null) {
                break;
            }
            else if(method.isAnnotationPresent(annotation)) {
                found = method;
            }
        }
        return found;
    }

    public static Class<?> getClass(String commandClass) throws ClassNotFoundException {
        return Thread.currentThread().getContextClassLoader().loadClass(commandClass);
    }

    public Object run(String[] args) {
        if(args.length < 1) {
            return null;
        }
        final String commandName = args[0];
        final String[] commandArgs =
                (args.length > 2) ? Arrays.copyOfRange(args,1,args.length) : new String[0];
        return run(commandName,commandArgs);
    }

    public Object run(String commandName, String[] commandArgs) {
        Object command;
        CommandReference reference = registry.get(commandName);
        if(reference == null) {
            throw new CommandNotFoundException(commandName);
        }
        try {
            command = reference.commandClass.newInstance();
        } catch (InstantiationException e) {
            throw new CommandInstantiationException(e);
        } catch (IllegalAccessException e) {
            throw new CommandInstantiationException(e);
        }

        inject(command,commandArgs);

        return execute(reference, command);
    }

    void inject(Object command, String[] commandArgs) {
        argumentProcessor.injectArguments(command,commandArgs);
        serviceRegistry.injectServices(command);
    }

    public Object execute(CommandReference reference, Object command) {
        Object[] varargs = {};
        Object out = null;
        try {
            try {
                if(reference.before != null)
                    reference.before.invoke(command,varargs);
            } catch (IllegalAccessException e) {
                throw new CommandSyntaxErrorException(reference.before);
            } catch (InvocationTargetException e) {
                throw new CliCommandException(reference.info.name(),reference.before,e.getCause());
            } finally {
                if(reference.run != null)
                    out = reference.run.invoke(command,varargs);
            }
        } catch (IllegalAccessException e) {
            throw new CommandSyntaxErrorException(reference.run);
        } catch (InvocationTargetException e) {
            throw new CliCommandException(reference.info.name(),reference.run,e.getCause());
        } finally {
            try {
                if(reference.after != null)
                    reference.after.invoke(command,varargs);
            } catch (IllegalAccessException e) {
                throw new CommandSyntaxErrorException(reference.after);
            } catch (InvocationTargetException e) {
                throw new CliCommandException(reference.info.name(),reference.after,e.getCause());
            }
        }
        return out;
    }

    public String useage() {
        String newline = System.getProperty("line.separator");
        StringBuffer out = new StringBuffer();
        for(String name : registry.keySet()) {
            out.append(name);
            out.append(newline);
        }
        return out.toString();
    }

    public void registerDirectory(final String directoryName) {
        Collection<File> scripts = findScripts(directoryName);
        for(File script : scripts) {
            CommandReference ref = commandReference(
                    scriptLoader.load(script)
            );
            register(ref);
        }
    }

    public Collection<File> findScripts(String directoryName) {
        LinkedList<File> scripts = new LinkedList<File>();
        File root = new File(directoryName);
        if(root.isDirectory()) {
            LinkedList<File> dirs = new LinkedList<File>();
            dirs.push(root);
            while(!dirs.isEmpty()) {
                File dir = dirs.pop();
                File[] children = dir.listFiles();
                for(File child : children) {
                    if(child.isDirectory()) {
                        dirs.push(child);
                    } else if (child.getName().endsWith(".groovy")) {
                        scripts.push(child);
                    }
                }
            }
        }
        return scripts;
    }
}
