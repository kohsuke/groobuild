target = "target/classes"

task("clean") {
    delete(dir:target)
}