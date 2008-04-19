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
import java.util.Map.Entry;

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
    private final Map<String,Task> tasks = new HashMap<String,Task>();

    private final Session session;

    public GrooProject(Session session) {
        this.session = session;
    }

    /**
     * Expose "ant" to the build script so that the script
     * can refer to Ant tasks even when they have colliding names
     * in this project.
     */
    public AntBuilder getAnt() {
        return session.antBuilder;
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
        if(res!=null)
            load(session.parse(res));

        throw new IOException("No such script: "+name);
    }

    public void load(ClosureScript definition) {
        definition.setDelegate(this);
        definition.run();
    }

    /**
     * Defines a task. This is a binding for Groovy.
     */
    public void task(Map<String,Dependency> decl, Closure c) {
        if(decl.size()!=1)
            throw new IllegalArgumentException("Size 1 map is expected but got "+decl);

        Entry<String,Dependency> e = decl.entrySet().iterator().next();

        task(e.getKey(),e.getValue(),c);
    }

    public void task(String taskName, Dependency dep, Closure c) {
        ScriptTask t = new ScriptTask(this,taskName,dep);
        t.add(c);
        tasks.put(taskName,t);
    }

    /**
     * Defines a task without dependencies.
     */
    public void task(String target, Closure c) {
        task(target,null,c);
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
        File f = new File(name);
        if(f.exists()) {
            t = new FileTask(this,f);
            tasks.put(name,t);
            return t;
        }

        throw new NoSuchTaskException(this,name);
    }

    public void attain(String... taskNames) {
        for (String s : taskNames)
            attain(s);
    }

    public void attain(String taskName) throws NoSuchTaskException {
        getTask(taskName).attain();
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
            return getAnt().invokeMethod(name,args);
        }
    }
}
