package zolter;

import org.apache.tools.ant.types.FileSet;

import java.io.File;

/**
     * {@link Path} whose location is staically known.
 */
public class PathConstant extends AbstractPathImpl {
    public final File value;

    public PathConstant(File value) {
        this.value = value;
    }

    public PathConstant _(String relativePath) {
        return new PathConstant(new File(value,relativePath));
    }

    public PathConstant resolve() {
        return this;
    }

    public FileSet toFileSet() {
        FileSet fs = new FileSet();
        fs.setDir(value);
        return fs;
    }
}
