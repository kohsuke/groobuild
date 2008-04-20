package groobuild;

import java.util.Date;

/**
 * @author Kohsuke Kawaguchi
 */
public interface Task {

    GrooProject getProject();

    Dependency getDependency();

    /**
     * Attains this task by executing it if necessary.
     *
     * @param knownAs
     *      The name of this task, known to the caller. Can be null
     *      if the caller is using this as anonymous. In GrooBuild,
     *      name is not an inherent property of the task, but it is
     *      only in the eyes of the beholder, as evident from the syntax
     *      "foo = task { ... }"
     */
    void attain(String knownAs);

    Date timestamp();

    public static final Date NEW = new Date(Long.MAX_VALUE);
    public static final Date OLD = new Date(0);
}
