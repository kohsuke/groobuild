package groobuild;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * @author Kohsuke Kawaguchi
 */
public class Main {
    public static void main(String[] args) throws IOException {
        File dir = new File(".");
        File buildScript = findBuildScript(dir);

        ExpandableClassLoader ecl = new ExpandableClassLoader(Main.class.getClassLoader());

        Binding binding = new Binding();
        CompilerConfiguration cc = new CompilerConfiguration();
        cc.setScriptBaseClass(GrooProject.class.getName());
        GroovyShell shell = new GroovyShell(ecl,binding,cc);

        GrooProject s = (GrooProject)shell.parse(buildScript);
        s.run();

        s.attain(args);
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
