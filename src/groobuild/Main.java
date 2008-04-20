package groobuild;

import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.Argument;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/**
 * @author Kohsuke Kawaguchi
 */
public class Main {
    @Option(name="-debug")
    public boolean debug;

    @Option(name="-verbose")
    public boolean verbose;

    @Argument
    public List<String> targets = new ArrayList<String>();

    public static void main(String[] args) throws IOException {
        System.exit(new Main().doMain(args));
    }

    private int doMain(String[] args) throws IOException {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("gbd [options...] [targets...]");
            parser.printUsage(System.err);
            return -1;
        }

        File buildScript = findBuildScript(new File("."));

//        new core();

        DefaultLogger logger = Session.createDefaultLogger();
        if(verbose)
            logger.setMessageOutputLevel(Project.MSG_VERBOSE);
        if(debug)
            logger.setMessageOutputLevel(Project.MSG_DEBUG);

        Session session = new Session(Main.class.getClassLoader(),logger);
        // this is too late because Ant wants to see tools.jar in the same classloader
        // see CompilerAdapterFactory
        // session.addToolsJar();
        GrooProject p = new GrooProject(session,buildScript.getParentFile());
        p.load(session.parse(buildScript));

        // TODO: detect cyclic execution of tasks
        p.attain(targets);

        return 0;
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
