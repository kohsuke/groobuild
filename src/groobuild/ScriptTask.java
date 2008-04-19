package groobuild;

import groovy.lang.Closure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Task that executes the groovy script once.
 * 
 * @author Kohsuke Kawaguchi
 */
public class ScriptTask extends AbstractTaskImpl {

    /**
     * Actions that represents this task.
     */
    private final List<Closure> actions = new ArrayList<Closure>();

    private boolean attained;

    public ScriptTask(GrooProject scope, String name, Dependency dep) {
        super(scope,name,dep);
    }

    public ScriptTask(GrooProject scope, String name) {
        this(scope,name,null);
    }

    /**
     * Intended for the custom task sub-class.
     */
    protected ScriptTask() {
        // obtains the current project from the parsing context,
        // and make it anonymous
        this(GrooProject.getCurrent(),null);
    }

    public void add(Closure a) {
        actions.add(a);
    }

    public void attain() {
        if(attained)
            // optimization. if we know that this task and its dependencies
            // have already been attained, there's no point in doing dependency.attain() 
            return;

        dependency.attain();

        if(isAttained())
            return; // nothing to do.

        System.out.println("Running "+name);

        for (Closure action : actions) {
            action.setDelegate(scope);
            action.call(this);
        }

        attained = true;
    }

    protected boolean isAttained() {
        return attained;
    }
}
