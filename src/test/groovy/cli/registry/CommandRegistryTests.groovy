package cli.registry

import cli.annotations.Command
import com.beust.jcommander.Parameter
import cli.annotations.Run
import org.junit.Test
import com.beust.jcommander.JCommander
import groovy.mock.interceptor.MockFor
import groovy.mock.interceptor.StubFor

/**
 * @author Shawn Hartsock
 */
class CommandRegistryTests {
    def svcs = [:]
    ArgumentProcessor argProcessor = [
            injectArguments: { def command, String[] arguments ->
                new JCommander(command, arguments);
            }
    ] as ArgumentProcessor

    @Test
    void testRegistryRegister() {
        ServiceRegistry svcReg = [
                regiser: { String name, Object service->
                    svcs[name] = service
                },
                lookup: {String name ->
                    svcs[name]
                },
                injectServices: { def object ->
                    svcs.each { key, val ->
                        object.properties[key] = val
                    }
                }

        ] as ServiceRegistry;

        def registry = new CommandRegistry([:],svcReg,argProcessor)
        def frobRef = registry.get("frob")
        assert frobRef == null
        frobRef = registry.register(Frobnicate)
        assert frobRef != null
        assert frobRef.before == null
        assert frobRef.run != null
        assert frobRef.after == null
        assert frobRef.info.name() == "frob"

        def out = registry.run("frob",(String[]) [])

        assert out == 'nothing'

        out = registry.run("frob",(String[]) ['--value','value'])

        assert out == 'value'
    }

    @Test
    void testStubFor() {
        def argProcStub = new StubFor(ArgProc)
        argProcStub.demand.injectArguments { Object cmd, String[] args ->
            cmd.value = 'value'
            return
        }
        argProcStub.use {
            ArgumentProcessor proc = new ArgProc()
            def cmd = [value:'nothing']
            proc.injectArguments(cmd,(String[]) ['1','2','3'])
            assert cmd.value == 'value'
        }
    }
}
// example

class ArgProc implements ArgumentProcessor {
    @Override
    void injectArguments(Object command, String[] arguments) {
        throw new RuntimeException("not implemented!")
    }
}

// command class for testing...
@Command(name="frob",description="foo bar")
class Frobnicate {
    @Parameter(names=['--value','-v'],description="some value")
    String value;

    @Run
    def doIt() {
        value?:'nothing'
    }

}

