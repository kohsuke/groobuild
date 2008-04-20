package groobuild;

import groovy.lang.Closure;
import groovy.lang.GroovyObjectSupport;
import groovy.lang.MissingMethodException;
import groovy.util.AntBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map.Entry;

import groobuild.ProxyTask.Resolver;

/**
 * Project definition.
 *
 * <p>
 * This object also serves as the delegate when we execute task definitions
 * as closures.
 *
 * @author Kohsuke Kawaguchi
 */
public class GrooProject extends GroovyObjectSupport {
    //public final Project antProject = new Project();
    /**
     * Expose "ant" to the build script so that the script
     * can refer to Ant tasks even when they have colliding names
     * in this project.
     */
    public final AntBuilder ant;

    private final Map<String,Task> tasks = new HashMap<String,Task>();

    private final Session session;

    private static ThreadLocal<GrooProject> PARSING_SCOPE = new ThreadLocal<GrooProject>();

    public final File baseDir;


    public GrooProject(Session session, File baseDir) {
        this.session = session;
        this.baseDir = baseDir;
        this.ant = session.createAntBuilder();
    }

    public Session getSession() {
        return session;
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
    public FileTask _(String path) {
        return new FileTask(this,toFile(path));
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

    /**
     * Defines a task. This is a binding for Groovy.
     */
    public Task task(Map<String,Dependency> decl, Closure c) {
        if(decl.size()!=1)
            throw new IllegalArgumentException("Size 1 map is expected but got "+decl);

        Entry<String,Dependency> e = decl.entrySet().iterator().next();

        return task(e.getKey(),e.getValue(),c);
    }

    public Task task(String taskName, Dependency dep, Closure c) {
        ScriptTask t = new ScriptTask(this, dep);
        t.add(c);
        tasks.put(taskName,t);
        return t;
    }

    /**
     * Defines a task without dependencies.
     */
    public Task task(String target, Closure c) {
        return task(target,null,c);
    }

    /**
     * Defines anonymous task.
     */
    public Task task(Closure c) {
        return task(null,null,c);
    }

    public ProxyTask proxy(final Closure c) {
        return new ProxyTask(new Resolver() {
            public FileTask resolve() {
                return FileTask.coerce(GrooProject.this,c.call());
            }
        });
    }

    // TODO: allow partial task definition

    public Dependency depends(String... args) {
        return new Dependency(this,args);
    }

    /**
     * Obtains the task that matches the given name.
     */
    public Task getTask(String name) {
        // first check for the named task
        Task t = tasks.get(name);
        if(t!=null)     return t;

        // is this a file?
        File f = toFile(name);
        if(f.exists()) {
            t = new FileTask(this,f);
            tasks.put(name,t);
            return t;
        }

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
        getTask(taskName).attain(taskName);
    }

    /**
     * Defines a task. <tt>define("foo",task)</tt> is the same as
     * <tt>foo=task</tt>, and normally the latter is preferrable,
     * but when the name includes characters that are not allowed
     * as a token, this verbose form becomes necessary.
     */
    public void define(String name, Task task) {
        Task old = tasks.get(name);
        if (old instanceof ProxyTask) {
            // redefining FileTask
            ProxyTask proxy = (ProxyTask) old;
            proxy.resolver = ProxyTask.literal((FileTask)task);
            return;
        }

        if (task instanceof FileTask) {
            // place the delegate so that we can redefine the actual path value later
            task = new ProxyTask((FileTask) task);
        }

        tasks.put(name,task);
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
        Task t = tasks.get(property);
        if(t!=null)
            return t;
        return super.getProperty(property);
    }

    public void setProperty(String property, Object newValue) {
        if(newValue instanceof Task) {
            define(property,(Task)newValue);
        }
        super.setProperty(property, newValue);
    }

    public static GrooProject getCurrent() {
        GrooProject project = PARSING_SCOPE.get();
        if(project==null)
            throw new Error("No project available in scope");
        return project;
    }
}
