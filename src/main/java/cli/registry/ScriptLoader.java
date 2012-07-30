package cli.registry;

import java.io.File;

/**
 * @author Shawn Hartsock
 */
public interface ScriptLoader {
    Class load(File script);
}
