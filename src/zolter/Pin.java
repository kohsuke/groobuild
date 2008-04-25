package zolter;

import org.apache.tools.ant.types.FileSet;

/**
 * Represents the logical input/output of a particular task.
 *
 * <p>
 * Some pins expect a single {@link Path} as a value, whereas others could
 * expect multiple {@link FileSet}s, and so on.
 *
 * <p>
 * {@link Pin} is sticky so that the value can be set to a pin with the assignment operator.
 *
 * @author Kohsuke Kawaguchi
 */
public interface Pin extends Sticky {
    Task owner();
    String name();

    /**
     * When the build script does <tt>compile.output = value</tt>
     * then it'll be executed as <tt>comile.getPin("output").assign(value)</tt>.
     */
    Object assign(Object rhs);
}
