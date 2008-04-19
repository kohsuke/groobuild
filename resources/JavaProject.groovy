target = dir("target/classes")

// TODO: if only Groovy had anonymous class...
class CompileTask extends groobuild.CustomTask {
    // compiler options. passed as attributes to <javac> task
    def options = [:]

    // classpath
    def classpath = []

    def sources = [project.dir("src/main/java")]

    void setVersion(v) {
        options.source=options.target=v.toString()
    }

    void execute() {
        javac( [source:"1.5",
                target:"1.5",
                srcdir:sources.join(':'),
                destdir:project.target]+options )
    }
}

compile = new CompileTask()


task("clean") {
    delete(dir:target)
}