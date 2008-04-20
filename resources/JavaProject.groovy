import groobuild.Task

target = _("target")

// TODO: if only Groovy had anonymous class...
class CompileTask extends groobuild.CustomTask {
    /**
     * Compiles options. These are attributes of &lt;javac> task.
     */
    def options = [
        source:"1.5",
        target:"1.5",
        destdir:getTarget(),
    ]

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

    /**
     * Gets the real direct value of the target field.
     * I couldn't figure out how to write the getTarget method
     * without having this method.
     */
    private Task realTarget() { return this.target }

    /**
     * Gets the indirect reference to the target directory.
     * The indirection allows users to modify the target directory,
     * after "compile.target" has been used in various places
     */
    Task getTarget() {
        // TODO: CustomTask.invokeMethod should delegate to project, too
        return project.proxy { -> this.realTarget() }
    }

    CompileTask _for(v) {
        version = v
        return this
    }

    CompileTask from(Object[] args) {
        sources = args.toList()
        return this
    }

    CompileTask to(dir) {
        target = dir
        return this
    }

    /**
     * Adds dependencies to the compilation. Note that this is additive.
     */
    CompileTask with(Object[] dependencies) {
        classpath += dependencies
        return this
    }

    void execute() {
        mkdir(dir:target)
        // TODO: modify AntBuilder so that toPath happens implicitly
        javac( [
                srcdir:toPath(sources),
                classpath:toPath(classpath)] + options )
    }
}

compile = new CompileTask().from(_("src/main/java")).to(target._("classes"))

compileTest = new CompileTask().from(_("src/test/java")).to(target._("test-classes"))
    .with(compile.target)


clean = task {
    delete(dir:target)
}