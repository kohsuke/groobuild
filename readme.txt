Tricky part is how to handle something like target/classes/**/*.class
when they don't exist yet.

Think of this as a graph?

Reuse Ant tasks underneath to do the actual work.
OTOH, we need to be able to declaratively specify classpath and compiler
output directory, so that other tasks can infer information from there.