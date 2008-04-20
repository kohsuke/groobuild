target = _("target")

// using closure so that variables like 'target' and 'compile' can be replaced later
compile = new CompileTask()
        .from(_("src/main/java"))
        .to({->target._("classes")})

compileTest = new CompileTask()
        .from(_("src/test/java"))
        .to({->target._("test-classes")})
        .with({->compile.output})

// TODO: compile task should yield the output directory

clean = task {
    delete(dir:target)
}