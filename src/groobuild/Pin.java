package groobuild;

import groovy.lang.GroovyObjectSupport;

/**
 *
 * @author Kohsuke Kawaguchi
 */
public class Pin extends GroovyObjectSupport {
    public final CustomTask parent;
    public final String name;

    public Pin(CustomTask parent, String name) {
        this.parent = parent;
        this.name = name;
    }
}
