import groobuild.CustomTask

class CompileTask extends CustomTask {
    /**
     * Compiles options. These are attributes of &lt;javac> task.
     */
    def options = [
        source:"1.5",
        target:"1.5"
    ]

    CompileTask() {
        // classpath
        this.classpath = inputPin().ofList()

        // list of source directories
        this.sources = inputPin().ofList()

        /**
         * This is aliased to "this", so that
         * you can write {@code compile.output} where another task
         * expects an output from the compiler. This is just so that
         * the core reads better.
         */
        this.output = outputPin();
    }


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

    CompileTask to(dir) {
        options.destdir = dir
        return this
    }

    /**
     * Adds dependencies to the compilation. Note that this is additive.
     */
    CompileTask with(Object[] dependencies) {
        classpath += dependencies
        return this
    }

    Object execute() {
        mkdir(dir:options.destdir)
        // TODO: modify AntBuilder so that toPath happens implicitly
        // and allow closure to be set for lazy evaluation
        javac( [
                srcdir:toPath(sources),
                classpath:toPath(classpath)] + options )

        // yields the class files
        return options.destdir
    }
}
