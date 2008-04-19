package groobuild;

import groovy.lang.GroovyObjectSupport;

import java.util.Date;

/**
 * @author Kohsuke Kawaguchi
 */
public abstract class AbstractTaskImpl extends GroovyObjectSupport implements Task {
    protected final GrooProject project;
    protected final String name;
    protected final Dependency dependency;

    public AbstractTaskImpl(GrooProject project, String name, Dependency dependency) {
        this.project = project;
        this.name = name;
        if(dependency==null)   dependency = new Dependency(project);
        this.dependency = dependency;
    }

    public String getName() {
        return name;
    }

    public Dependency getDependency() {
        return dependency;
    }

    public GrooProject getProject() {
        return project;
    }

    public Date timestamp() {
        return dependency.timestamp();
    }
}
