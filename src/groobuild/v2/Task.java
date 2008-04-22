package groobuild.v2;

import java.util.Map;
import java.util.HashMap;

/**
 * TODO: ConfigurableTarget?
 *
 * @author Kohsuke Kawaguchi
 */
public class Task extends Target {
    private final Map<String,Pin> pins = new HashMap<String,Pin>();
}
