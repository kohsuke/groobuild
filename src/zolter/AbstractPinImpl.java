package zolter;

/**
 * @author Kohsuke Kawaguchi
 */
abstract class AbstractPinImpl implements Pin {
    protected final Task owner;
    /**
     * Once computed, pin name is cached.
     */
    private String name;

    protected AbstractPinImpl(Task owner) {
        this.owner = owner;
    }

    public final Task owner() {
        return owner;
    }

    public String name() {
        if(name==null)
            name = owner.getName(this);
        return name;
    }
}
