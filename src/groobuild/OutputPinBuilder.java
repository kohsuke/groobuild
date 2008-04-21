package groobuild;

/**
 * @author Kohsuke Kawaguchi
 */
public class OutputPinBuilder extends PinBuilder {
    Pin build(CustomTask parent, String name) {
        return new OutputPin(parent,name);
    }
}
