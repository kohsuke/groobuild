package groobuild;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.util.AntBuilder;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.NoBannerLogger;
import org.apache.tools.ant.Project;
import org.codehaus.groovy.control.CompilerConfiguration;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Kohsuke Kawaguchi
 */
public class Session {
    public final ExpandableClassLoader classLoader;

    private final GroovyShell parser;
    private BuildListener logger;

    public Session(ClassLoader classLoader) {
        this(classLoader,createDefaultLogger());
    }

    public Session(ClassLoader classLoader, BuildListener logger) {
        this.classLoader = new ExpandableClassLoader(classLoader);
        this.parser = createParser();
        this.logger = logger;
    }

    public static DefaultLogger createDefaultLogger() {
        NoBannerLogger logger = new NoBannerLogger();
        logger.setMessageOutputLevel(Project.MSG_INFO);
        logger.setOutputPrintStream(System.out);
        logger.setErrorPrintStream(System.err);
        return logger;
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

    /**
     * Creates a new {@link AntBuilder} for the new project.
     */
    /*package*/ AntBuilder createAntBuilder() {
        Project project = new Project();
        project.addBuildListener(logger);
        project.init();
        project.getBaseDir();
        return new AntBuilder(project);
    }
}
