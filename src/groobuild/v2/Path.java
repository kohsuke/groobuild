package groobuild.v2;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents a file path whose actual location is determined lazily.
 *
 * <p>
 * {@link Path} is {@link Attainable}, which has the semantics
 * similar to that of make &mdash; meaning updating/generating the file.
 *
 * @author Kohsuke Kawaguchi
 */
public abstract class Path implements Attainable {
    /**
     * Determines the actual location and returns it
     * as {@link Constant}.
     */
    public abstract Constant resolve();

    /**
     * Returns {@link Path} that represents a subpath from this path.
     */
    public abstract Path _(String relativePath);

    /**
     * {@link Path} whose actual location is known.
     *
     * This representation is immutable.
     */
    public static final class Constant extends Path {
        public final File path;

        /**
         * Pre-requisites for attaining this path.
         */
        public final List<Attainable> dependencies = new ArrayList<Attainable>();

        public Constant(File path) {
            this.path = path;
        }

        public Constant resolve() {
            return this;
        }

        public void attain() {
            // TODO
            throw new UnsupportedOperationException();
        }

        public Path _(String relativePath) {
            return new Constant(new File(path,relativePath));
        }
    }

    /**
     * {@link Path} whose actual location may change
     * after the path is used elsewhere.
     *
     * <p>
     * Image this:
     * <pre>
     * target = "target/classes";
     * compiler.output = target;
     * target = "build/classes";
     * </pre>
     * <p>
     * We'd like the compiler.output to change to the new value.
     */
    public static final class Variable extends Path {
        public File path;

        /**
         * Pre-requisites for attaining this path.
         *
         * TODO: do we want this? what's the semantics of {@link #resolve()}?
         * Any room for reuse between this and Constant?
         */
        public final List<Attainable> dependencies = new ArrayList<Attainable>();

        public Constant resolve() {
            return new Constant(path);
        }

        public Path _(String relativePath) {
            // TODO
            throw new UnsupportedOperationException();
        }

        public void attain() {
            // TODO
            throw new UnsupportedOperationException();
        }
    }
}
