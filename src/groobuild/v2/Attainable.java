package groobuild.v2;

/**
 * @author Kohsuke Kawaguchi
 */
public interface Attainable {
    void attain();

// if dependencies are attained implicitly when the attain method is called,
// what's the point of exposing it here?
//    Collection<Attainable> getDependencies();
}
