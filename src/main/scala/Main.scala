

object Main extends App {
  val thread = new Thread {
    override def run(): Unit = {
      if (!Params.SAVE && !Params.DISPLAY_GRAPH) {
        println("This configuration does not have sense, either SAVE or DISPLAY_GRAPH must be true")
        println("Exiting program...")
        return
      }
      val world: World = new World(1000, 10, (200, 20, 8))
      println(s"Basic Reproduction Number: R0=${world.calcR0()}")
      if (Params.DISPLAY_GRAPH) new GraphUI(world)
      else {
        var worldSpinning = true
        while (worldSpinning) worldSpinning = world.iterationLoop()
      }
    }
  }
  thread.start()
}