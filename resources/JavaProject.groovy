if(!target)
    target = _("target")

// TODO: if only Groovy had anonymous class...
class CompileTask extends groobuild.CustomTask {
    // compiler options. passed as attributes to <javac> task
    def options = [:]

    // classpath
    def classpath = []

    // list of source directories
    def sources;

    // where to produce the output?
    def target;

    /**
     * Convenience method to set both the source/target JDK version.
     *
     * <p>
     * Use it like <tt>compile.version = 1.4</tt>
     */
    void setVersion(v) {
        options.source=options.target=v.toString()
    }

    CompileTask _for(v) {
        version = v
        return this
    }

    CompileTask from(Object[] args) {
        sources = args.toList()
        return this
    }

    void execute() {
        mkdir(dir:target)
        javac( [source:"1.5",
                target:"1.5",
                srcdir:sources.join(':'),
                destdir:target]+options )
    }
}

compile = new CompileTask(
        sources:[_("src/main/java")],
        target: target._("classes"));

compileTest = new CompileTask(
        sources:[project._("src/test/java")],
        target: target._("test-classes"));

clean = task {
    delete(dir:target)
}