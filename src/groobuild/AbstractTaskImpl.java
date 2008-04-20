package groobuild;

import groovy.lang.GroovyObjectSupport;
import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.Target;

import java.util.Date;

/**
 * @author Kohsuke Kawaguchi
 */
// TODO: merge with Task?
public abstract class AbstractTaskImpl extends GroovyObjectSupport implements Task {
    protected final GrooProject project;
    protected final Dependency dependency;
    private boolean attained;

    public AbstractTaskImpl(GrooProject project, Dependency dependency) {
        this.project = project;
        if(dependency==null)   dependency = new Dependency(project);
        this.dependency = dependency;
    }

    public Dependency getDependency() {
        return dependency;
    }

    public GrooProject getProject() {
        return project;
    }

    /**
     * Short cut for accessing {@link Session}.
     */
    public Session getSession() {
        return project.getSession();
    }

    public Date timestamp() {
        return dependency.timestamp();
    }

    public void attain(String knownAs) {
        if(attained)
            // optimization. if we know that this task and its dependencies
            // have already been attained, there's no point in doing dependency.attain()
            return;

        dependency.attain();

        if(isAttained())
            return; // nothing to do.

        BuildListener l = getSession().getLogger();
        Target t = new Target();
        t.setName(knownAs);
        l.targetStarted(new BuildEvent(t));

        try {
            execute();
            l.targetFinished(new BuildEvent(t));
        } catch (RuntimeException e) {
            l.targetFinished(createBuildEvent(t,e));
            throw e;
        } catch (Error e) {
            l.targetFinished(createBuildEvent(t,e));
            throw e;
        }


        attained = true;
    }

    private BuildEvent createBuildEvent(Target t, Throwable e) {
        BuildEvent event = new BuildEvent(t);
        event.setException(e);
        return event;
    }

    protected abstract void execute();

    protected boolean isAttained() {
        return attained;
    }
}
