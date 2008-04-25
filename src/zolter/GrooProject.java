package zolter;

import groovy.lang.Closure;
import groovy.lang.GroovyObjectSupport;
import groovy.lang.MissingMethodException;
import groovy.util.AntBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * Project definition.
 *
 * <p>
 * This object also serves as the delegate when we execute task definitions
 * as closures.
 *
 * @author Kohsuke Kawaguchi
 */
// TODO: split it into two --- one that remembers information about single execution,
// and the other as the owner of multiple projects
public class GrooProject extends GroovyObjectSupport {
    //public final Project antProject = new Project();
    /**
     * Expose "ant" to the build script so that the script
     * can refer to Ant tasks even when they have colliding names
     * in this project.
     */
    public final AntBuilder ant;

    private final Map<String,Attainable> attainables = new HashMap<String,Attainable>();

    public final Session session;

    private static ThreadLocal<GrooProject> PARSING_SCOPE = new ThreadLocal<GrooProject>();

    public final File baseDir;


    public GrooProject(Session session, File baseDir) {
        this.session = session;
        this.baseDir = baseDir;
        this.ant = session.createAntBuilder();
    }

    /**
     * Loads the definition from another GrooBuild script.
     */
    public void inherit(String name) throws IOException {
        // check built-in resources
        URL res = getClass().getClassLoader().getResource(name + ".groovy");
        if(res!=null) {
            load(session.parse(res));
            return;
        }

        throw new IOException("No such script: "+name);
    }

    public void load(ClosureScript definition) {
        definition.setDelegate(this);
        
        GrooProject old = PARSING_SCOPE.get();
        PARSING_SCOPE.set(this);
        try {
            definition.run();
        } finally {
            PARSING_SCOPE.set(old);
        }
    }

    /**
     * <tt>_("src/main/java")</tt> means a directory.
     */
    public Path _(String path) {
        return new PathConstant(toFile(path));
    }

    /**
     * Resolves a relative path from {@link #baseDir}.
     */
    private File toFile(String path) {
        return new File(baseDir,path);
    }

    /**
     * Self reference so that "project" can be used like "this"
     * but with the proper semantics (because 'this' would refer to
     * {@link ClosureScript})
     */
    public GrooProject getProject() {
        return this;
    }

//    /**
//     * Defines a task. This is a binding for Groovy.
//     */
//    public Target task(Map<String,?> params, Closure c) {
//        if(params.size()!=1)
//            throw new IllegalArgumentException("Size 1 map is expected but got "+decl);
//
//        Entry<String,Dependency> e = decl.entrySet().iterator().next();
//
//        return task(e.getKey(),e.getValue(),c);
//    }

    /**
     * Defines a task without dependencies.
     */
    public Target task(Closure c) {
        Target t = new Target();
        t.action = c;
        return t;
    }


    /**
     * Obtains the task that matches the given name.
     */
    public Target getTask(String name) {
        // first check for the named task
        Attainable a = attainables.get(name);
        if (a instanceof Target) {
            Target t = (Target) a;
            return t;
        }

        // TODO: do we want to support make-like file target?

        throw new NoSuchTaskException(this,name);
    }

    public void attain(String... taskNames) {
        attain(Arrays.asList(taskNames));
    }

    public void attain(Collection<String> taskNames) {
        for (String s : taskNames)
            attain(s);
    }

    public void attain(String taskName) throws NoSuchTaskException {
        session.attain(getTask(taskName));
    }

    /**
     * Defines a task. <tt>define("foo",task)</tt> is the same as
     * <tt>foo=task</tt>, and normally the latter is preferrable,
     * but when the name includes characters that are not allowed
     * as a token, this verbose form becomes necessary.
     */
    public void define(String name, Attainable task) {
        setProperty(name,task);
    }

    /**
     * Pretend as if all the ant tasks are available as the first class method,
     * so that people can write something like the following:
     *
     * <pre>
     * task("clean") {
     *   delete ...
     * }
     * </pre>
     */
    public Object invokeMethod(String name, Object args) {
        try {
            return super.invokeMethod(name, args);
        } catch (MissingMethodException e) {
            return ant.invokeMethod(name,args);
        }
    }

    /**
     * Pretend as if all the tasks are properties.
     */
    public Object getProperty(final String property) {
        Attainable a = attainables.get(property);
        if(a!=null)
            return a;
        return super.getProperty(property);
    }

    public void setProperty(String property, Object newValue) {
        Attainable old = attainables.get(property);
        if (old instanceof Sticky)
            newValue = ((Sticky)old).assign(newValue);

        if(newValue instanceof Attainable)
            attainables.put(property,(Attainable)newValue);

        super.setProperty(property, newValue);
    }

    public static GrooProject getCurrent() {
        GrooProject project = PARSING_SCOPE.get();
        if(project==null)
            throw new Error("No project available in scope");
        return project;
    }
}
