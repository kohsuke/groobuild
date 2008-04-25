package zolter;

import java.util.List;

/**
 * @author Kohsuke Kawaguchi
 */
public interface Attainable {
    void attain(Session session);

    /**
     * Read-write list of other {@link Attainable}s that this object depends on.
     *
     * <p>
     * TODO: is there any harm in making this mutatable when this is this close to the root?
     *
     * TODO: what if we make this monotonic --- can add but not subtract?
     */
    List<Attainable> prerequisites();
}
