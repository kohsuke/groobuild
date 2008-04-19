package groobuild;

import java.io.File;
import java.util.Date;

/**
 * {@link Task} that creates/updates a file.
 * @author Kohsuke Kawaguchi
 */
public class FileTask extends ScriptTask {
    public final File target;

    public FileTask(GrooBuildScript scope, File f) {
        super(scope, f.getPath());
        this.target = f;
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
}
