Mandelbrojt
===========

Yet another Mandelbrot Set rendering program (written in Clojure).

Why?
====

So it's been done to death.  Why write yet another program to generate
the Mandelbrot set?  Because I want to.  And because I like it.  What
more reason do I need.

But if you want something that sounds more legitimate: I want practice
using Clojure's concurrency and parallel processing semantics.  That,
and access to Java's GUI widget set makes it easier to render
graphically than in other langauges and environments.  Granted, this
will start life by emitting only text as output, or writing to an
uncompressed PNM file.  But I plan to get to the interactive graphical
output once I learn how to do that from Clojure.

"But the Mandebrot set, on the JVM?  Won't it be horribly slow?", I
can already hear you asking.  Maybe, but I don't care.  And frankly, I
intend to prove that (mostly) wrong anyway.  Yes, it will always be
faster in C or hand-coded assembly, but I want to see how closely I
can approach that "ideal".

The name
========

I was trying to come up with a name for this, something a bit more
interesting and disambiguating than just calling it "mandelbrot".  And
I wanted to indicate it was written in Clojure in some subtle way,
without beating everyone over the head with it.  I was just about to
call it HowieBot (as a lame reference to Howie Mandel - yes, I think
I'm funny), when I happened upon this gem in the Wikipedia article on
Benoit Mandelbrot: his uncle's name was Szolem Mandelbrojt.  Perfect.
A valid and appropriate alternate spelling of the family name (if I
wanted to be obscure, I would've named it "Szolem", but I don't).

Running
=======

Assuming you ran `lein uberjar` first, here is an example of how to
run it:

    java -server -jar target/mandelbrojt-0.1.0-SNAPSHOT-standalone.jar -f 1440x900.pbm -w 1440 -h 900

The `-server` option is needed for larger resolution output, because
the default profile the JVM picks doesn't allocate enough heap
(alternatively, you can allocate the amount of heap you think you'll
need).  For smaller resolutions, you can omit this.  I'm hoping to
make this code more memory efficient later - this is just a first
pass.

Progress
========

As of the current state of this code, it makes lots of naive
assumptions, and isn't really optimized at all.

The first area that's ripe for optimization is probably the orbit
iterating code.  It's very wasteful in that it probably keeps the
entire sequence in memory, when all it really needs at any given time
is the last point generated in the sequence.

The next area that could use work is convergence detection.  I only
used the simplest and most basic means of detecting whether the orbit
is destined for infinity (a simple cutoff), but I could probably use
more sophisticated techniques to detect whether the sequence is likely
to converge.

Third is the wasteful way in which I generate the output.  It's all
probably stored as one snapshot before being written to disk, rather
than being streamed as it's generated.

In general, I need to find ways to stream the data from one function
to the next.  I haven't even begun trying to use the concurrency
primitives.
