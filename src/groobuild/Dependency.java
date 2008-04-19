package groobuild;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author Kohsuke Kawaguchi
 */
public class Dependency implements Iterable<Task> {
    public final GrooProject scope;

    private final List<String> list = new ArrayList<String>();

    public Dependency(GrooProject scope) {
        this.scope = scope;
    }

    public Dependency(GrooProject scope, String... args) {
        this(scope);
        list.addAll(Arrays.asList(args));
    }

    public boolean isEmpty() {
        return list.isEmpty(); 
    }

    public void addAll(Dependency d) {
        list.addAll(d.list);
    }

    /**
     * Attains all the dependencies.
     */
    public void attain() {
        for (String arg : list)
            scope.attain(arg);
    }

    /**
     * Computes the combined timestamp of all dependencies.
     * That is, the newest timestamp among all dependencies.
     */
    public Date timestamp() {
        Date dt = null;
        for (Task t : this) {
            Date ts = t.timestamp();
            if(dt==null)
                dt= ts;
            else
            if(dt.compareTo(ts)<0)
                dt = ts;    // pick up bigger one
        }
        if(dt==null)
            // no dependency
            dt = Task.OLD;
        return dt;
    }

    public Iterator<Task> iterator() {
        return new Iterator<Task>() {
            private final Iterator<String> i = list.iterator();

            public boolean hasNext() {
                return i.hasNext();
            }

            public Task next() {
                return scope.getTask(i.next());
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
