package groobuild;

/**
 * @author Kohsuke Kawaguchi
*/
public final class InputPinBuilder extends PinBuilder {
    private boolean list;

    public InputPinBuilder ofList() {
        list = true;
        return this;
    }

    Pin build(CustomTask parent, String name) {
        return new InputPin(parent,name);
    }
}
