package zolter;

import org.apache.tools.ant.types.FileSet;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * Partial default implementation of {@link Path}.
 *
 * @author Kohsuke Kawaguchi
 */
abstract class AbstractPathImpl implements Path {
    private final List<Attainable> prerequisites = new ArrayList<Attainable>();

    public ZFileSet includes(String includes) {
        return new ZFileSetImpl(this,includes,null);
    }

    public ZFileSet fileSet(Map<String, ?> parameters) {
        throw new UnsupportedOperationException();
    }

    public ZFileSet excludes(String pattern) {
        return new ZFileSetImpl(this,null,pattern);
    }

    public List<Attainable> prerequisites() {
        return prerequisites;
    }

    public Path _(String relativePath) {
        return new RelativePath(this,relativePath);
    }

    public FileSet toFileSet() {
        return resolve().toFileSet();
    }

    public void attain(Session session) {
        // TODO: shall we define a Closure action here?

        // other than that, no-op
    }
}
