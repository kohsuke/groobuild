package zolter;

import org.apache.tools.ant.types.FileSet;

/**
 * Represents Ant's {@link FileSet} but the concrete location is determined lazily.
 *
 * @author Kohsuke Kawaguchi
 */
public interface ZFileSet {
    /**
     * Determines the concrete location and returns it as {@link FileSet}.
     */
    FileSet toFileSet();

    /**
     * Creates a file set that uses this path as the base directory.
     */
    ZFileSet includes(String includes);

    /**
     * Returns a new {@link ZFileSet} that represents this file set plus the given exclusion.
     */
    ZFileSet excludes(String excludes);
}
