
JavaProject = GrooProject {
    target = dir("target/classes")

    clean = task {
        delete(dir:target)
    }
}