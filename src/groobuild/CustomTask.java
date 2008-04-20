package groobuild;

import groovy.lang.MissingMethodException;

import java.io.File;
import java.util.List;

/**
 * @author Kohsuke Kawaguchi
 */
public abstract class CustomTask extends AbstractTaskImpl {
    /**
     * Intended for the custom task sub-class.
     */
    protected CustomTask() {
        // obtains the current project from the parsing context,
        // and make it anonymous
        super(GrooProject.getCurrent(), null);
    }

    /**
     * If this task produces a file/directory,
     * this method returns it.
     *
     * @see FileTask#coerce(GrooProject, Object) 
     */
    public FileTask produces() {
        return null;
    }

    protected String toPath(List<?> args) {
        StringBuilder buf = new StringBuilder();
        for (Object arg : args) {
            if(buf.length()>0)  buf.append(File.pathSeparatorChar);
            buf.append(toFile(arg));
        }
        return buf.toString();
    }

    private File toFile(Object arg) {
        // make sure the file is attained before we use it
        return FileTask.coerce(project,arg).build();
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
