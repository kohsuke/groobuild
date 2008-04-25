package zolter;

/**
 * @author Kohsuke Kawaguchi
 */
public class NoSuchTaskException extends IllegalArgumentException {
    public final GrooProject scope;
    public final String taskName;

    public NoSuchTaskException(GrooProject scope, String taskName) {
        super("No such task "+taskName);
        this.scope = scope;
        this.taskName = taskName;
    }
}
