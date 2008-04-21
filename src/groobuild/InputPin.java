package groobuild;

/**
 * @author Kohsuke Kawaguchi
 */
public class InputPin extends Pin {
    public FileTask value;

    public InputPin(CustomTask parent, String name) {
        super(parent,name);
    }
}
