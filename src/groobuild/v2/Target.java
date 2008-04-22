package groobuild.v2;

import groovy.lang.Closure;

import java.util.List;
import java.util.ArrayList;

/**
 * @author Kohsuke Kawaguchi
 */
public class Target implements Attainable {
    private Target redefinedBy;

    public final List<Attainable> dependencies = new ArrayList<Attainable>();

    private Closure action;

    public void attain() {
        // TODO
        throw new UnsupportedOperationException();
    }
}
