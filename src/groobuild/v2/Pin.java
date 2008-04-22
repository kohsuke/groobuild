package groobuild.v2;

/**
 *
 * <p>
 * {@link Pin} is a {@link Path} 
 *
 * @author Kohsuke Kawaguchi
 */
public abstract class Pin extends Path {
    /**
     * The actual location of this pin.
     */
    private Path value;

    /**
     * Owner.
     */
    public final Task task;

    public String getName() {
        // TODO: determine the name via reflection from task's fields
        throw new UnsupportedOperationException();
    }
}
