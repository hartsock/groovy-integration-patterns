package cli.registry

import cli.annotations.Run

/**
 * @author Shawn Hartsock
 */
class CommandHolder {
    def closure;
    @Run
    void run() {
        closure()
    }
}
