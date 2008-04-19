package groobuild;

/**
 * @author Kohsuke Kawaguchi
 */
public class NoSuchTaskException extends IllegalArgumentException {
    public final GrooBuildScript scope;
    public final String taskName;

    public NoSuchTaskException(GrooBuildScript scope, String taskName) {
        super("No such task "+taskName);
        this.scope = scope;
        this.taskName = taskName;
    }
}
