package cli.registry

/**
 * @author Shawn Hartsock
 */
class GroovyScriptLoader implements ScriptLoader {
    Class load(File script) {
        String name = script.name
        String path = script.absolutePath
        new GroovyClassLoader().parseClass("""
import cli.annotations.*

@Command(name="${name.replace(".groovy","")}",description="${path}")
class ${name.replace(".groovy","Groovy")} {
    @Run
    void run() {
        ${script.text}
    }
}""");
    }
}
