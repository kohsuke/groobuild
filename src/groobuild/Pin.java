package groobuild;

import groovy.lang.GroovyObjectSupport;
import zolter.Sticky;

/**
 * Represents input/output terminal of a {@link Task}.
 *
 * <p>
 * {@link Pin} is {@link Sticky sticky} because <tt>task.pin = value</tt> means
 * the pin value assignment, instead of replacing a pin.
 *
 * @author Kohsuke Kawaguchi
 */
public abstract class Pin extends GroovyObjectSupport implements Sticky {
    public final CustomTask parent;
    private String name;

    public Pin(CustomTask parent) {
        this.parent = parent;
    }

}
