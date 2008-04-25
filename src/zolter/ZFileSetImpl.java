package zolter;

import org.apache.tools.ant.types.FileSet;

/**
 * @author Kohsuke Kawaguchi
 */
final class ZFileSetImpl implements ZFileSet {
    private final ZFileSet base;
    private final String includes;
    private final String excludes;


    public ZFileSetImpl(ZFileSet base, String includes, String excludes) {
        this.base = base;
        this.includes = includes;
        this.excludes = excludes;
    }

    public FileSet toFileSet() {
        FileSet fs = base.toFileSet();
        if(includes!=null)
            fs.createInclude().setName(includes);
        if(excludes!=null)
            fs.createExclude().setName(excludes);
        return fs;
    }

    public ZFileSet excludes(String pattern) {
        return new ZFileSetImpl(this,null,pattern);
    }
}
