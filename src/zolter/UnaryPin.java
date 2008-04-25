package zolter;

import org.apache.tools.ant.types.FileSet;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * {@link Pin} that holds a single {@link Path} value.
 *
 * <p>
 * {@link UnaryPin} itself can be used as a {@link Path} so that
 * the build script can assign one pin to another pin.
 *
 * @author Kohsuke Kawaguchi
 */
public abstract class UnaryPin extends AbstractPinImpl implements Path {
    
    private Path path;
    private final List<Attainable> prerequisites = new ArrayList<Attainable>();

    protected UnaryPin(Task owner) {
        super(owner);
    }

    public Path getPath() {
        return path;
    }

    /**
     * Assigns the given path to this pin.
     * This pin will read from or write to this path.
     */
    void setPath(Path p) {
        this.path = p;
    }

    public Object assign(Object rhs) {
        setPath(Paths.coerse(rhs));
        return this;
    }

    public Path _(String relativePath) {
        return new RelativePath(this,relativePath);
    }

    public PathConstant resolve() {
        return path.resolve();
    }

    public ZFileSet includes(String includes) {
        return new ZFileSetImpl(this,includes,null);
    }

    public ZFileSet fileSet(Map<String, ?> parameters) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public ZFileSet excludes(String excludes) {
        return new ZFileSetImpl(this,null,excludes);
    }

    public FileSet toFileSet() {
        return path.toFileSet();
    }

    public List<Attainable> prerequisites() {
        return prerequisites;
    }

    public void attain(Session session) {
        // TODO: should I define Closure action?
        // other than that, no-op.
    }
}
