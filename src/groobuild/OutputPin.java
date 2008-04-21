package groobuild;

/**
 * @author Kohsuke Kawaguchi
 */
public class OutputPin extends Pin {
    private FileTask value;

    public OutputPin(CustomTask parent, String name) {
        super(parent,name);
    }

    public void setValue(FileTask f) {
        this.value = f;
    }

    public FileTask resolve() {
        parent.attain(null);    // TODO: name?
        return value;
    }
}
