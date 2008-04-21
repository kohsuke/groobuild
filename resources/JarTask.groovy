import groobuild.CustomTask

/**
 *
 *
 * @author Kohsuke Kawaguchi
 */
class JarTask extends CustomTask {

    def manifest

    protected Object execute() {
        throw new UnsupportedOperationException();
    }
}