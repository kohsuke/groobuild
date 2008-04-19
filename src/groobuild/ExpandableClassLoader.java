package groobuild;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * {@link ClassLoader} that can dynamically add new path elements later.
 *
 * @author Kohsuke Kawaguchi
 */
public class ExpandableClassLoader extends URLClassLoader {
    public ExpandableClassLoader(ClassLoader parent) {
        super(new URL[0], parent);
    }

    public void addURL(URL url) {
        super.addURL(url);
    }
}
