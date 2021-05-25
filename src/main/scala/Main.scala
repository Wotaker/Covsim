

object Main extends App {
  val thread = new Thread {
    override def run(): Unit = {
      val gui: GraphUI = new GraphUI(new World(20, 1, (5, 4, 2)))
    }
  }
  thread.start()
}