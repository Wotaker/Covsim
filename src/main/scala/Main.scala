

object Main extends App {
  val thread = new Thread {
    override def run(): Unit = {
      if (!Params.SAVE && !Params.DISPLAY_GRAPH) {
        println("This configuration does not have sense, either SAVE or DISPLAY_GRAPH must be true")
        println("Exiting program...")
        return
      }
      val world: World = new World(100, 1, (20, 4, 3))
      if (Params.DISPLAY_GRAPH)  GraphUI.run(world)
      else {
        var worldSpinning = true
        while (worldSpinning) worldSpinning = world.iterationLoop()
      }
    }
  }
  thread.start()
}