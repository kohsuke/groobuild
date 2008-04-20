package groobuild;

import groovy.lang.MissingMethodException;

import java.io.File;
import java.util.List;

/**
 * @author Kohsuke Kawaguchi
 */
public abstract class CustomTask extends AbstractTaskImpl {
    private boolean attained;

    /**
     * Intended for the custom task sub-class.
     */
    protected CustomTask() {
        // obtains the current project from the parsing context,
        // and make it anonymous
        super(GrooProject.getCurrent(),null,null);
    }

    public void attain() {
        if(attained)
            // optimization. if we know that this task and its dependencies
            // have already been attained, there's no point in doing dependency.attain()
            return;

        dependency.attain();

        System.out.println("Running "+name);

        execute();

        attained = true;
    }

    /**
     * To be implemented in the custom task Groovy class.
     */
    protected abstract void execute();

    protected String toPath(List<?> args) {
        StringBuilder buf = new StringBuilder();
        for (Object arg : args) {
            if(buf.length()>0)  buf.append(File.pathSeparatorChar);
            buf.append(toFile(arg));
        }
        return buf.toString();
    }

    private File toFile(Object arg) {
        if (arg instanceof File) {
            return (File) arg;
        }
        if (arg instanceof String) {
            arg = project._((String) arg);
        }
        if (arg instanceof ProxyTask) {
            arg = ((ProxyTask) arg).getDelegate();
        }
        if (arg instanceof FileTask) {
            return ((FileTask) arg).build();
        }
        throw new IllegalArgumentException("Don't know how to convert "+arg+" to a file");
    }

    /**
     * Allows the execute method to use Ant tasks
     * as if they are methods.
     *
     * TODO: limit this only from inside the execute() method. 
     */
    public Object invokeMethod(String name, Object args) {
        try {
            return super.invokeMethod(name, args);
        } catch (MissingMethodException e) {
            return project.ant.invokeMethod(name,args);
        }
    }
}
