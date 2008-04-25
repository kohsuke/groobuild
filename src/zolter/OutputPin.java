package zolter;

/**
 * @author Kohsuke Kawaguchi
 */
public class OutputPin extends UnaryPin {

    /**
     * Where should the output go?
     */
    public Path path;

    protected OutputPin(Task owner) {
        super(owner);
        // attaining the output value requires the execution of the task
        prerequisites().add(owner);
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }
}
