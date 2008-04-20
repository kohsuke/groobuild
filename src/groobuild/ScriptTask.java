package groobuild;

import groovy.lang.Closure;

import java.util.ArrayList;
import java.util.List;

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

    public ScriptTask(GrooProject scope, Dependency dep) {
        super(scope, dep);
    }

    public ScriptTask(GrooProject scope) {
        this(scope, null);
    }


    public void add(Closure a) {
        actions.add(a);
    }

    @Override
    protected void execute() {
        for (Closure action : actions) {
            action.setDelegate(project);
            action.call(this);
        }
    }
}
