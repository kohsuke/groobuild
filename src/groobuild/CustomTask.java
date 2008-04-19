package groobuild;

import groovy.lang.MissingMethodException;

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
            return scope.ant.invokeMethod(name,args);
        }
    }
}
