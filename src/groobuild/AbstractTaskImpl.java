package groobuild;

import groovy.lang.GroovyObjectSupport;
import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Project;

import java.util.Date;
import java.text.MessageFormat;

import zolter.GrooProject;
import zolter.Session;

/**
 * @author Kohsuke Kawaguchi
 */
// TODO: merge with Task?
public abstract class AbstractTaskImpl extends GroovyObjectSupport implements Task {
    protected final GrooProject project;
    protected final Dependency dependency;
    private boolean attained;
    private Object result;

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
        return project.session;
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

        BuildListener l = getSession().getLogger();

        if(isAttained()) {
            log(Project.MSG_DEBUG,"{0} is considered up-to-date",knownAs);
            return; // nothing to do.
        }

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

    private void log(int level, String msg, Object... args) {
        BuildEvent e = new BuildEvent(project.ant.getAntProject());
        e.setMessage(MessageFormat.format(msg,args),level);
        getSession().getLogger().messageLogged(e);
    }

    private BuildEvent createBuildEvent(Target t, Throwable e) {
        BuildEvent event = new BuildEvent(t);
        event.setException(e);
        return event;
    }

    /**
     * Executes the task, and if it yields something
     * that can be coersed into {@link FileTask} it returns that.
     */
    protected abstract Object execute();

    protected boolean isAttained() {
        return attained;
    }
}
