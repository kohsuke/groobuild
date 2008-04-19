def target = "target/classes"

compile = task {
    // this should be entirely doable in Groovy
    // because other plugins would be doing something very similar
    ant.compile( [source:"1.5",target:"1.5"]+compile.options )
}

task("clean") {
    delete(dir:target)
}