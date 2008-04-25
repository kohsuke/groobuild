package zolter;

import groovy.lang.MissingMethodException;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.lang.reflect.Field;

/**
 * @author Kohsuke Kawaguchi
 */
public class Task extends Target {
    public final GrooProject project;

    public Task(GrooProject project) {
        this.project = project;
    }

    /**
     * For custom tasks defined in Groovy, so that they won't have to write
     * a pass-through constructor.
     */
    protected Task() {
        this(GrooProject.getCurrent());
    }


    protected OutputPin outputPin() {
        return new OutputPin(this);
    }


    /**
     * Used by {@link Pin} to query its name.
     */
    /*package*/ String getName(Pin pin) {
        for( Class c=this.getClass(); c!=Object.class; c=c.getSuperclass()) {
            for( Field f : c.getDeclaredFields()) {
                if(f.get(this)==pin)
                    return f.getName();
            }
        }
        return null;
    }

    /**
     * Allows the execute method to use Ant tasks
     * as if they are methods.
     *
     * TODO: limit this only from inside the execute() method.
     */
    public Object invokeMethod(String name, Object args) {
        try {
            return super.invokeMethod(name, args);
        } catch (MissingMethodException e) {
            return project.ant.invokeMethod(name,args);
        }
    }

    public void setProperty(String property, Object newValue) {
        Pin old = pins.get(property);
        if (old!=null) {
            old.assign(newValue);
            return;
        }

        super.setProperty(property, newValue);
    }
}
