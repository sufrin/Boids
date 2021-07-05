# Boids
## Boid simulation using ThreadCSO
Simple example of a hybrid concurrent implementation using 
[ThreadCSO](https://github.com/sufrin/ThreadCSO)
barriers, semaphores, and channels.

The flock program simulates a collection of flocking birds, as
originally specified in [Boids](https://en.wikipedia.org/wiki/Boids). 

Once started it displays the flock at the specified framerate, and
permits various parameters to be set dynamically from the keyboard
of the terminal from which it was started. Each bird is simulated
by a threadcso process (see `FLOCKBARRIER.scala`). The display
controller, the interaction controller, and the keyboard are also
threadcso processes.  Birds and the display synchronise on a barrier
(twice per round), and the keyboard communicates by a channel with
the interaction controller that sets the bird parameters. There is
no synchronisation involved here, though a purist might complain
that different birds might thereby read different parameters on the
same round.

The program is started (assuming you keep the threadcso library
in the same place as I do) by:

       scala -cp ~/local/lib/threadcso.jar:FLOCK.jar FLOCK *flocksize* (default 250)

One or more parameters can be set on a single keyboard line at any
time: just write a sequence of paramname=value settings separated
by spaces. The initial default parameter values are:

       R=40          frame rate (ms/frame)
       drag=0.9      drag coefficient
       co=0.002      Cohesiveness
       se=8.0        Min separation
       al=0.05       Tendency to align with visible neighbours
       f=0.7500*PI   Field of vision
       r=150.0       Range of vision
       V=10.0        Max velocity
       v=5.0         Min velocity

Watch what happens as you (gradually) change parameters. All sorts of
interesting emergent behaviours can be observed.

*Bernard Sufrin, February 2016*

**HISTORY**: Gavin Lowe used a simpler variant of Boids as an example
of Barrier synchronization sometime during his first tenure of the
Oxford Concurrent Programming course. Gavin's variant had race
conditions around the use of the display that led to bizarre visual
artefacts due to severe problems of synchronization between the
Java GUI thread and the program itself.  I corrected those mistakes
when I reinherited the Oxford Concurrent Programming course.

