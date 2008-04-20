package groobuild;

import java.io.File;
import java.io.IOException;

/**
 * @author Kohsuke Kawaguchi
 */
public class Main {
    public static void main(String[] args) throws IOException {
        File dir = new File(".");
        File buildScript = findBuildScript(dir);

        Session session = new Session(Main.class.getClassLoader());
        // this is too late because Ant wants to see tools.jar in the same classloader
        // see CompilerAdapterFactory
        // session.addToolsJar();
        GrooProject p = new GrooProject(session);
        p.load(session.parse(buildScript));

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
