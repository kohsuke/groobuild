package groobuild;

import groovy.lang.MissingMethodException;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kohsuke Kawaguchi
 */
public abstract class CustomTask extends AbstractTaskImpl {
    /**
     * When the custom task is assigned a name (like {@code foo=compile(...)})
     * we remember its name.
     */
    private String name;

    /**
     * Pins defined on this task.
     */
    private final Map<String,Pin> pins = new HashMap<String,Pin>();

    /**
     * Intended for the custom task sub-class.
     */
    protected CustomTask() {
        // obtains the current project from the parsing context,
        // and make it anonymous
        super(GrooProject.getCurrent(), null);
    }

    /**
     * Creates a new input pin.
     */
    protected final PinBuilder inputPin() {
        return new InputPinBuilder();
    }

    /**
     * Creates a new output pin.
     */
    protected final PinBuilder outputPin() {
        return new OutputPinBuilder();
    }

    /**
     * If this task produces a file/directory,
     * this method returns it.
     *
     * @see FileTask#coerce(GrooProject, Object) 
     */
    public FileTask produces() {
        return null;
    }

    protected String toPath(List<?> args) {
        StringBuilder buf = new StringBuilder();
        for (Object arg : args) {
            if(buf.length()>0)  buf.append(File.pathSeparatorChar);
            buf.append(toFile(arg));
        }
        return buf.toString();
    }

    private File toFile(Object arg) {
        // make sure the file is attained before we use it
        return FileTask.coerce(project,arg).build();
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
        {// "task.pin = value" is interpreted as "task.pin.value = value"
            Pin pin = pins.get(property);
            if(pin !=null) {
                pin.setProperty("value",newValue);
                return;
            }
        }

        // intercept the assignment of the new pin definition and register it
        if (newValue instanceof PinBuilder) {
            PinBuilder builder = (PinBuilder) newValue;
            Pin pin = builder.build(this,property);
            pins.put(property,pin);
            newValue = pin;
        }

        super.setProperty(property, newValue);
    }
}
