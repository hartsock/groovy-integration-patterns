package cli

import cli.registry.CommandRegistry
import cli.registry.ServiceRegistry
import com.beust.jcommander.JCommander
import cli.registry.ArgumentProcessor
import cli.registry.GroovyScriptLoader

/**
 * @author Shawn Hartsock
 */
class Main {
    static scriptsDir = System.getProperty('user.home') + System.getProperty('file.separator') + 'scripts'
    static svcs = [:];

    static void main(String[] args) {
        def registry = new CommandRegistry(
                [:],

                [
                        regiser: { String name, Object service ->
                            svcs[name] = service
                        },
                        lookup: {String name ->
                            svcs[name]
                        },
                        injectServices: { def object ->
                            svcs.each { key, val ->
                                if(object.properties.containsKey(key))
                                    object.properties[key] = val
                            }
                        }
                ] as ServiceRegistry,

                [
                        injectArguments: { def command, String[] arguments ->
                            new JCommander(command, arguments);
                        }
                ] as ArgumentProcessor,
                new GroovyScriptLoader()
        )
        registry.init()
        registry.registerDirectory(scriptsDir)
        def arguments = args.length>1?args[1..args.length]:[]
        def commandName = args?args.first():null
        if(commandName) {
            println registry.run(commandName,(String[]) arguments.toArray())
        } else {
            println registry.useage()
        }
    }
}
