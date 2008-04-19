def target = "target/classes"

// TODO: if only Groovy had anonymous class...
class CompileTask extends groobuild.ScriptTask {
    // compiler options. passed as attributes to <javac> task
    def options = [:]

    // classpath
    def classpath = []

    void setVersion(v) {
        options.source=options.target=v.toString()
    }

    void attain() {
        // TODO: this shall become javac
        println( [source:"1.5",target:"1.5"]+options)
    }
}

compile = new CompileTask()

task("clean") {
    delete(dir:target)
}