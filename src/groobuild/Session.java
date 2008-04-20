package groobuild;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;

/**
 * @author Kohsuke Kawaguchi
 */
public class Session {
    public final ExpandableClassLoader classLoader;

    private final GroovyShell parser;

    public Session(ClassLoader classLoader) {
        this.classLoader = new ExpandableClassLoader(classLoader);
        this.parser = createParser();
    }

    private GroovyShell createParser() {
        Binding binding = new Binding();
        CompilerConfiguration cc = new CompilerConfiguration();
        cc.setScriptBaseClass(ClosureScript.class.getName());
        return new GroovyShell(classLoader,binding,cc);
    }

    /**
     * Parses a GrooBuild script.
     */
    public ClosureScript parse(URL projectDef) throws IOException {
        return (ClosureScript) parser.parse(projectDef.openStream(), projectDef.toExternalForm());
    }

    /**
     * Parses a GrooBuild script.
     */
    public ClosureScript parse(File projectDef) throws IOException {
        return (ClosureScript) parser.parse(projectDef);
    }

    /**
     * Tries to locate <tt>tools.jar</tt> and add that to {@link #classLoader}.
     */
    public void addToolsJar() throws MalformedURLException {
        File javaHome = new File(System.getProperty("java.home"));
        File toolsJar = new File(javaHome, "../lib/tools.jar");
        if(toolsJar.exists())
            classLoader.addURL(toolsJar.toURI().toURL());
    }
}
