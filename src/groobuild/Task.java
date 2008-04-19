package groobuild;

import java.util.Date;

/**
 * @author Kohsuke Kawaguchi
 */
public interface Task {

    /**
     * Name of this task.
     */
    String getName();

    GrooProject getScope();

    Dependency getDependency();

    void attain();

    Date timestamp();

    public static final Date NEW = new Date(Long.MAX_VALUE);
    public static final Date OLD = new Date(0);
}
