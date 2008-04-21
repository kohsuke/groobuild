package groobuild;

/**
 * Tunnel an uninitialized pin until it's assigned to {@link CustomTask}.
 * @author Kohsuke Kawaguchi
 */
public abstract class PinBuilder {
    abstract Pin build(CustomTask parent, String name);

}
