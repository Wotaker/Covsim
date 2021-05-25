

object Main extends App {
  val thread = new Thread {
    override def run(): Unit = {
      val gui: GraphUI = new GraphUI(new World(200, 1, (50, 14, 4)))
    }
  }
  thread.start()
}