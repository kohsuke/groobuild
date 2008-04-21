target = _("target")

compile = new CompileTask()
        .from("src/main/java")
        .to(target._("classes"))

compileTest = new CompileTask()
        .from("src/test/java")
        .to(target._("test-classes"))
        .with(compile.output)

// TODO: compile task should yield the output directory

clean = task {
    delete(dir:target)
}