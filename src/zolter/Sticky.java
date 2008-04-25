package zolter;

/**
 * Marks those objects that overrides the assignment operator.
 *
 * <p>
 * Normally <tt>a=b</tt> means the content of the variable/property 'a' will be set to 'b'
 * and its old value will be gone, but if the current variable on the left-hand side
 * is {@link Sticky}, the same code will mean <tt>a=a.assign(b)</tt>.
 *
 * <p>
 * This is the basis for the wiring support in Zolter, so that people can write like
 * <tt>compile.output = "abc/def"</tt>.
 *
 * <p>
 * Obviously to implement this semantics requires the support from the property owner
 * (in this case 'compile'.)
 *
 *
 * @author Kohsuke Kawaguchi
 */
public interface Sticky {
    Object assign(Object rhs);
}
