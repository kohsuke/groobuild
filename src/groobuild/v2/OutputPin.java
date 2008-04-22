package groobuild.v2;

/**
 * @author Kohsuke Kawaguchi
 */
public final class OutputPin extends Pin {
    public void attain() {
        task.attain();
    }
}
