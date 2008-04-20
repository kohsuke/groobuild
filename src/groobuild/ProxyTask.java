package groobuild;

import groovy.lang.GroovyObjectSupport;
import groovy.lang.Closure;

import java.util.Date;

/**
 * Indirection to {@link FileTask},
 * so that the wiring of dependencies can be done before the actual path locations
 * are modified.
 *
 * <p>
 * We need this instead of simple {@link Closure} so that we can handle
 * {@link #_(String)} correctly.
 *
 * @author Kohsuke Kawaguchi
 */
final class ProxyTask extends GroovyObjectSupport implements Task {
    interface Resolver {
        FileTask resolve();
    }

    public static Resolver literal(final FileTask f) {
        return new Resolver() {
            public FileTask resolve() {
                return f;
            }
        };
    }

    /*package*/ Resolver resolver;


    protected ProxyTask(Resolver resolver) {
        this.resolver = resolver;
    }

    protected ProxyTask(FileTask ft) {
        this(literal(ft));
    }

    public void redefine(GrooProject project, Object dir) {
        resolver = literal(FileTask.coerce(project,dir));
    }

    public FileTask getDelegate() {
        return resolver.resolve();
    }

    public String getName() {
        return getDelegate().getName();
    }

    public GrooProject getProject() {
        return getDelegate().getProject();
    }

    public Dependency getDependency() {
        return getDelegate().getDependency();
    }

    public void attain() {
        getDelegate().attain();
    }

    public Date timestamp() {
        return getDelegate().timestamp();
    }

    public String toString() {
        return getDelegate().toString();
    }

    public ProxyTask _(final String relativePath) {
        return new ProxyTask(new Resolver() {
            public FileTask resolve() {
                return getDelegate()._(relativePath);
            }
        });
    }

    public Object invokeMethod(String name, Object args) {
        return getDelegate().invokeMethod(name, args);
    }

    public void setProperty(String property, Object newValue) {
        getDelegate().setProperty(property, newValue);
    }

    public Object getProperty(String property) {
        return getDelegate().getProperty(property);
    }
}
