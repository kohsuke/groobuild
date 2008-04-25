package zolter;

/**
 * Factory for {@link Path}.
 *
 * @author Kohsuke Kawaguchi
 */
public class Paths {
    /**
     * Converts the given object to {@link Path}.
     *
     * <p>
     * This allows build scripts to be terse.
     */
    public static Path coerse(Object rhs) {
        throw new UnsupportedOperationException();
    }
}
