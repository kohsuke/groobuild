package zolter;

/**
 * {@link Path} that represents a relative path from another {@link Path}.
 * @author Kohsuke Kawaguchi
 */
final class RelativePath extends AbstractPathImpl {
    private final Path base;
    private final String relativePath;

    public RelativePath(Path base, String relativePath) {
        this.base = base;
        this.relativePath = relativePath;
    }

    public PathConstant resolve() {
        return base.resolve()._(relativePath);
    }
}
