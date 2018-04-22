# Snail Roadmap

1. Snail VM and compiler written in Java
2. The compiler part of Snail is rewritten in itself, so we just need the
   VM in Java
3. The VM is rewritten in C++ so the dependency on Java is removed.
4. Design another language Slug which has Snail-like syntax but which
   compiles to C++. Slug will resemble Snail wherever possible, but
   otherwise have C++-like semantics.
5. Write a Slug-to-C++ compiler in Snail
6. Translate the VM from C++ to Slug. Now, Snail/Slug are kind of
   self-hosting, except for the compilation to C++.
7. Rewrite the Slug-to-C++ compiler into a Slug-to-C compiler.

At this point (if I ever get up to here!) I want to explore compilation
to native machine code (such as x64 assembly).

Other directions to branch off into would include an FFI (see LibFFI,
c2ffi), event-driven programming (LibUV), some kind of UI (maybe a GUI,
SDL-based? or a Web UI? a text mode UI, like curses??).

Ideally, I'd like Snail/Slug to be a completely self-hosting system, in
which even the underlying operating system is written in Snail/Slug, and
all the necessary development tools (editor, compiler, assembler, linker,
version control, diff/patch/search tools, issue tracker, code coverage,
profiling, debugging, build tool, etc) are too. However, I realise that is
an extremely ambitious goal, so odds are I am never going to achieve it.
