package zolter;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.util.AntBuilder;
import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.NoBannerLogger;
import org.apache.tools.ant.Project;
import org.codehaus.groovy.control.CompilerConfiguration;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * @author Kohsuke Kawaguchi
 */
public class Session {
    public final ExpandableClassLoader classLoader;

    private final GroovyShell parser;
    private BuildListener logger;
    private final Set<Attainable> attained = new HashSet<Attainable>();

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

    public void setLogger(BuildListener logger) {
        this.logger = logger;
    }

    public BuildListener getLogger() {
        return logger;
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
        if(logger!=null)
            project.addBuildListener(logger);
        project.init();
        project.getBaseDir();
        return new AntBuilder(project);
    }

    /**
     * Executes the session and attains all the specified targets.
     */
    public void execute(GrooProject p, Collection<String> targets) {
        logger.buildStarted(new BuildEvent(p.ant.getAntProject()));

        // TODO: detect cyclic execution of tasks
        p.attain(targets);

        // TODO: pass in exception
        logger.buildFinished(new BuildEvent(p.ant.getAntProject()));
    }

    public void attain(Attainable a) {
        // TODO: cycle detection
        attain(a.prerequisites());

        if(attained.add(a))
            a.attain(this);
    }

    public void attain(Collection<? extends Attainable> attainables) {
        for (Attainable a : attainables)
            attain(a);
    }
}
