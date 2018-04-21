# Snail Design Rationale

## Some design principles

* Try to avoid excessive complexity. I know that is hard in general, and
  especially hard for me. The boundary between necessary and excessive
  complexity is unclear, subjective, quite difficult to judge.
* Don't have too many data types, just enough to prove the point:
    * **String**
    * **Symbol**. Symbol is just string with somewhat different semantics,
      and some syntactic constraints applied.
    * **Integer** (say signed 64-bit). Other integer types, like big integers,
      unsigned integers, smaller integer types, can be useful in various
      circumstances, but aren't necessary to "prove the point".
    * We probably need a **binary** type (as opposed to text string).
      Strings will be valid Unicode (UTF-8, UTF-16, etc.) Binary is
      arbitrary binary data.
    * Instead of a boolean type, we can just use the symbol type, with
      "true" and "false" as symbols. Boolean is just a two-valued
      enumeration.
    * Floating point can be skipped for now. The same is true for any
      other non-integral data types, such as decimal types, rational
      types, complex numbers, fixed point, etc. (If we really need
      floating point at some point, we can start with IEEE double only.)
    * A dynamic **list** type, an **object** type.
    * We can skip a "set" type for now - lists suffice for this purpose.
    * We don't need a "map" type. We have objects. For data like JSON,
      encourage using static typing with schema mapping. If someone really
      needs a "map", a list of key-value pair objects can be used.
* Focus on semantics rather than syntax. Syntax can be easily changed.
* No threads, no concurrency:
    * The initial goal is to try to be self-hosting, i.e. write its own
      compiler. Compilers don't require threads or concurrency. (They
      can possibly use them to speed up compilation on multi-core
      machines, but that is a performance optimisation, not something
      for an initial prototype.)
    * If eventually concurrency is needed, suggest implementing it using
      a single-threaded event loop, as in node.js etc.
* No network access, etc. It is isn't necessary for the goal of writing
  a self-hosting compiler.

## Initial focus

* Make it as self-hosting as possible.
* Explores some ideas about memory management.
	* Mixture of handles, ownership and reference counting.

## Things not to implement (at least not yet)

* Macros. Good idea but avoid doing them until I really feel the need.
  When I do feel the need, they should work on expression tree not
  bytecode level.
* Lazy arguments. My idea is lazy resources are only valid when the
  allocating frame is still on the stack; closing that frame
  automatically cancels all lazy values it allocated. The lazy
  resource still exists but is marked as cancelled and any attempt to
  use it will error.
	* Maybe even better, raise error at scope exit if any lazy
	  have escaped. Trigger the error at escape point, not ages
          later when you try to use the escaped value. For each
	  lazy in our chain, look at enclosing document. If any other
	  task has a handle to the document, or our own task at a
	  higher scope, then the lazy is escaping. But this means a
	  lazy can't be a "simple resource", it has to be a
	  "complex one", because it knows which document(s) it belongs to.
* Coroutines (tasks). Note these don't cause circular reference issues
  because they are global (like symbol bindings). There are methods to
  get all tasks (task.getAll), to get a task by its ID (task.byId).
* Anonymous functions. These aren't closures because they have no access
  to the defining scope.
* Closures. A cross between a lazy and an anonymous function. A lazy is
  like a closure with no arguments. Just like a lazy, we cannot allow
  a closure to survive longer than its allocating scope due to our
  memory model. Since a lazy is just a zero-argument closure, we don't
  need lazy, we can just have closure, and lazy arguments is a form of
  auto-closure.
* Variadic functions. Good idea, but extra complexity at first. This
  includes optional arguments, argument default values, rest arguments.
* Keyword arguments. Another good idea, but again extra complexity.
* Multiple return values. Sometimes useful but too much complexity.
