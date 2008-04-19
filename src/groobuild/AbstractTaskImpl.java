package groobuild;

import java.util.Date;

/**
 * @author Kohsuke Kawaguchi
 */
public abstract class AbstractTaskImpl implements Task {
    protected final GrooBuildScript scope;
    protected final String name;
    protected final Dependency dependency;

    public AbstractTaskImpl(GrooBuildScript scope, String name, Dependency dependency) {
        this.scope = scope;
        this.name = name;
        if(dependency==null)   dependency = new Dependency(scope);
        this.dependency = dependency;
    }

    public String getName() {
        return name;
    }

    public Dependency getDependency() {
        return dependency;
    }

    public GrooBuildScript getScope() {
        return scope;
    }

    public Date timestamp() {
        return dependency.timestamp();
    }
}
