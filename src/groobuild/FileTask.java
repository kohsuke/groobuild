package groobuild;

import java.io.File;
import java.util.Date;

/**
 * {@link Task} that creates/updates a file.
 * @author Kohsuke Kawaguchi
 */
public class FileTask extends ScriptTask {
    public final File target;

    public FileTask(GrooProject project, File f) {
        super(project);
        this.target = f;
    }

    /**
     * Updates this file (attains this task) and then returns it.
     */
    public File build() {
        attain(target.getPath());
        return target;
    }

    @Override
    public Date timestamp() {
        if(target.exists())
            return new Date(target.lastModified());
        else
            // force the execution by pretending that this file is really old
            return OLD;
    }

    /**
     * If the file is newer than its dependencies, the task is considered attained.
     */
    protected boolean isAttained() {
        // equal time stamp should be considered attained --- imagine the copy operation for example.
        return super.isAttained() || dependency.timestamp().compareTo(timestamp()) <= 0;
    }

    /**
     * This makes the conversion implicit when a {@link FileTask} is
     * passed to Ant task as a parameter.
     */
    public String toString() {
        return target.getPath();
    }

    /**
     * Allows sub path to be obtained like {@code dir["relative"]}
     */
    public FileTask _(String relativePath) {
        return new FileTask(project,new File(target,relativePath));
    }

    public static FileTask coerce(GrooProject project, Object arg) {
        if (arg instanceof File) {
            return new FileTask(project, (File) arg);
        }
        if (arg instanceof String) {
            arg = project._((String) arg);
        }
        if (arg instanceof ProxyTask) {
            arg = ((ProxyTask) arg).getDelegate();
        }
        if (arg instanceof FileTask) {
            return (FileTask)arg;
        }
        throw new IllegalArgumentException("Don't know how to convert " + arg + " to a file");
    }
}
