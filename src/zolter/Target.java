package zolter;

import groovy.lang.Closure;
import groovy.lang.GroovyObjectSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kohsuke Kawaguchi
 */
public class Target extends GroovyObjectSupport implements Attainable {
    /**
     * Other {@link Attainable}s that this target depends on.
     */
    private final List<Attainable> prerequisites = new ArrayList<Attainable>();

    /**
     * The thing to be executed when the target is attained.
     */
    public Closure action;

    // TODO: what does it mean for a target, not task, to be redefined?
    /*package*/ Target redefinedBy;

    public List<Attainable> prerequisites() {
        return prerequisites;
    }

    public void attain(Session session) {
        action.call(this);
    }
}
