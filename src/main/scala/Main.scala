import Gui._
object Main extends App {
  val thread = new Thread {
    override def run(): Unit = {
      Gui.main()
      Gui.addBuildings("H",4)
      Gui.addBuildings("S",4)
      Gui.addBuildings("W",1)
      val world = new World(20, 1, (4, 4, 1))
      for (iter <- 0.until(25)) world.iterationLoop()
      world.printBuildings()
      world.printPopulation()
    }
  }

  thread.start()
}