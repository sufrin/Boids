import io.threadcso._
// Driver for boids simulations 
object FLOCK {
  def main(args: Array[String]) = 
  { var N = 250
    import scala.math.Pi 
    import Boid._
    import BBox._
    
    def setParam(arg: String): Unit = {
        if (arg.matches("[0-9]+")) N = arg.toInt else
        if (arg.matches("[0-9]+x[0-9]+")) 
        { val x = arg.indexOf('x')
          xSize = arg.substring(0, x).toInt
          ySize = arg.substring(x+1).toInt
        }
        else
        if (arg == "S+") Display.Synchronizing(true) else
        if (arg == "S-") Display.Synchronizing(false) else
        if (arg == "A-") Display.antiAlias=false else
        if (arg == "A+") Display.antiAlias=true else
        if (arg.startsWith("R="))    Rate = arg.substring(2).toInt else
        if (arg.startsWith("drag=")) drag = arg.substring(5).toDouble else
        if (arg.startsWith("co=")) Coh = arg.substring(3).toDouble else
        if (arg.startsWith("se=")) Sep = arg.substring(3).toDouble else
        if (arg.startsWith("al=")) Align = arg.substring(3).toDouble else
        if (arg.startsWith("r="))  range = arg.substring(2).toDouble else
        if (arg.startsWith("V="))  maxSpeed = arg.substring(2).toDouble else
        if (arg.startsWith("v="))  minSpeed = arg.substring(2).toDouble else 
        if (arg.startsWith("f="))  breadth = Pi * arg.substring(2).toDouble else
        Console.println(f"""
          R=$Rate\t\tframe rate (ms/frame)
          drag=$drag\tdrag coefficient 
          co=$Coh\tCohesiveness
          se=$Sep\tMin separation
          al=$Align\tTendency to align with visible neighbours
          f=${breadth/Pi}%1.4g*π\tField of vision 
          r=$range\tRange of vision
          V=$maxSpeed\t\tMax velocity
          v=$minSpeed\t\tMin velocity
         """)
    }
    
    def printParams = 
    {   val syncing = if (Display.Synchronizing) '+' else '-'
        val antialiasing = if (Display.antiAlias) '+' else '-'
        Console.println(
         "%d %dx%d R=%d S%c A%c drag=%4.3g co=%4.3g se=%4.3g al=%4.3g f=%4.3g r=%04.3g V=%4.3g v=%4.3g".
         format(N, xSize, ySize, Rate, syncing, antialiasing, drag, Coh, Sep, Align, breadth/Pi, range, maxSpeed, minSpeed)
        )
    }
    
    val kbd = OneOne[String] 
    val interact = proc {
        serve (
          kbd =?=> {
            cmd => {
              for (arg <- cmd.split("[ \t]+")) setParam(arg)
              printParams
            }
          }
        )
    }   
    
    for (arg <- args) setParam(arg)
    
    printParams
    (component.keyboard(kbd) || interact).fork    
    new FLOCKBARRIER(N).run
  }
}












