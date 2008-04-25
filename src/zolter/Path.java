package zolter;

import java.util.Map;

/**
 * Represents a file/directory whose concrete location is determined lazily.
 *
 * <p>
 * {@link Path} is attainable, and this is much like how you can
 * make a file in the make tool.
 *
 * @author Kohsuke Kawaguchi
 */
public interface Path extends ZFileSet, Attainable {
    /**
     * Obtains a relative path.
     */
    Path _(String relativePath);

    /**
     * Determines the concrete location.
     */
    PathConstant resolve();

    // for Groovy
    ZFileSet fileSet(Map<String,?> parameters);

}
