package zolter;

import org.apache.tools.ant.types.FileSet;

/**
 * {@link Path} whose actual value can be substituted later.
 *
 * <p>
 * {@ilnk Variable} can be sticky so that people can write
 * <pre>
 * target = "target"
 * compile.output = target._("classes")
 *
 * target = "build"
 * </pre>
 * ... and <tt>compile.output</tt> should now become build/classes.
 */
public class PathVariable extends AbstractPathImpl implements Sticky {
    public Path value;

    public Object assign(Object rhs) {
        value = Paths.coerse(rhs);
        return this;
    }

    public PathConstant resolve() {
        return value.resolve();
    }

    public FileSet toFileSet() {
        return value.toFileSet();
    }
}
