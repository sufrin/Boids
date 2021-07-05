import io.threadcso._
import scala.collection.mutable.Set

class FLOCKBARRIER(N: Int) {

  val boids = Set[Boid]()

  val random = new scala.util.Random();

  // Barrier for synchronisation of N boids and a display controller
  val barrier = new Barrier(N+1)

// Each round contains two barrier synchronisations.  
// After the first synchronisation, but before the second, 
// boids may read each others' states.  
// After the second synchronisation, boids may update their own states.

  // Boid #me controller process
  def BoidController(me: Int) = proc {
    // Set up this boid's initial state
    val myBoid = new Boid
    myBoid.init(random)
    boids += myBoid 
   
    // Main loop
    while (true) {
      barrier.sync()              // enter reading phase
      val state = myBoid.newState(boids)
      barrier.sync()              // enter writing phase
      myBoid.setState(state)
    }
  }

  // Display controller process
  val DisplayController = proc {
    barrier.sync()                // wait for boids to be initialised
    val display = new Display(boids)
    while (true) { 
       barrier.sync()             // reading
       display.draw() 
       sleep(Boid.Rate*milliSec)
       barrier.sync()             // writing
    }
  }

  // Put the system together
  val System = 
    || (for (i <- 0 until N) yield BoidController(i)) || DisplayController
    
  def run = System()

}








