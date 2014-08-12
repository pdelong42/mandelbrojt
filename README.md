Mandelbrojt
===========

Yet another Mandelbrot Set rendering program (written in Clojure).

Why?
====

So it's been done to death.  Why write yet another program to generate the
Mandelbrot set?  Because I want to.  And because I like it.  What more reason
do I need.

But if you want something that sounds more legitimate: I want practice using
Clojure's concurrency and parallel processing semantics.  That, and access to
Java's GUI widget set makes it easier to render graphically than in other
langauges and environments.  Granted, this will start life by emitting only
text as output.  But I plan to get to the graphical output once I learn how to
do that from Clojure.

"But Mandebrot set, on the JVM?  Won't it be horribly slow?", I can already
hear you asking.  Maybe, but I don't care.  And frankly, I intend to prove that
(mostly) wrong anyway.  Yes, it will always be faster in C or hand-coded
assembly, but I want to see how closely I can approach that "ideal".

The name
========

I was trying to come up with a name for this, something a bit more interesting
and disambiguating than just calling it "mandelbrot".  And I wanted to indicate
it was written in Clojure in some subtle way, without beating everyone over the
head with it.  I was just about to call it HowieBot (as a lame reference to
Howie Mandel - yes, I think I'm funny), when I happened upon this gem in the
Wikipedia article on Benoit Mandelbrot: his uncle's name was Szolem
Mandelbrojt.  Perfect.  A valid and appropriate alternate spelling of the
family name (if I wanted to be obscure, I would've named it "Szolem", but I
don't).
