package groobuild;

import java.io.File;
import java.io.IOException;

/**
 * @author Kohsuke Kawaguchi
 */
public class Main {
    public static void main(String[] args) throws IOException {
        File buildScript = findBuildScript(new File("."));

//        new core();

        Session session = new Session(Main.class.getClassLoader());
        // this is too late because Ant wants to see tools.jar in the same classloader
        // see CompilerAdapterFactory
        // session.addToolsJar();
        GrooProject p = new GrooProject(session,buildScript.getParentFile());
        p.load(session.parse(buildScript));

        // TODO: detect cyclic execution of tasks
        p.attain(args);
    }


    public static File findBuildScript(File dir) {
        for( ; dir!=null && dir.exists(); dir=dir.getParentFile()) {
            File buildScript = new File(dir,"build.gb");
            if(buildScript.exists())
                return buildScript;
        }
        return null;
    }
}
