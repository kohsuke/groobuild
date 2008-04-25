package zolter;

import groovy.lang.Binding;
import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.MissingMethodException;
import groovy.lang.MissingPropertyException;
import groovy.lang.Script;

/**
 * {@link Script} that performs method invocations and property access like {@link Closure} does.
 *
 * <p>
 * For example, when the script is:
 *
 * <pre>
 * a = 1;
 * b(2);
 * <pre>
 *
 * <p>
 * Using {@link ClosureScript} as the base class would run it as:
 *
 * <pre>
 * delegate.a = 1;
 * delegate.b(2);
 * </pre>
 *
 * ... whereas in plain {@link Script}, this will be run as:
 *
 * <pre>
 * binding.setProperty("a",1);
 * ((Closure)binding.getProperty("b")).call(2);
 * </pre>
 *
 * @author Kohsuke Kawaguchi
 */
public abstract class ClosureScript extends Script {
    private GroovyObject delegate;

    protected ClosureScript() {
        super();
    }

    protected ClosureScript(Binding binding) {
        super(binding);
    }

    /**
     * Sets the delegation target.
     */
    public void setDelegate(GroovyObject delegate) {
        this.delegate = delegate;
    }

    public GroovyObject getDelegate() {
        return delegate;
    }

    public Object invokeMethod(String name, Object args) {
        if(delegate==null)
            return super.invokeMethod(name, args);
        try {
            return delegate.invokeMethod(name,args);
        } catch (MissingMethodException mme) {
            return super.invokeMethod(name, args);
        }
    }

    public Object getProperty(String property) {
        if(delegate==null)
            return super.getProperty(property);
        try {
            return delegate.getProperty(property);
        } catch (MissingPropertyException e) {
            return super.getProperty(property);
        }
    }

    public void setProperty(String property, Object newValue) {
        if(delegate==null) {
            super.setProperty(property,newValue);
            return;
        }
        try {
            delegate.setProperty(property,newValue);
        } catch (MissingPropertyException e) {
            super.setProperty(property,newValue);
        }
    }
}
