import org.graphstream.ui.view.ViewerListener
import org.graphstream.graph.Graph
import org.graphstream.graph.implementations.SingleGraph
import org.graphstream.ui.view.Viewer
import javax.swing.text.View
import org.graphstream.ui.view.ViewerPipe
import org.graphstream.graph.Node
import scala.collection.mutable.ListBuffer
import StateSIR._
import org.graphstream.graph.Edge


class GraphUI(val world: World) extends ViewerListener {

  // Initialize
  var loop: Boolean = true

  System.setProperty("org.graphstream.ui", "swing")
  var graph: Graph = new SingleGraph("SimulationCity")

  // Sets the styling as in file "/src/main/scala/style/stylesheet.css"
  graph.setAttribute(
    "ui.stylesheet", 
    "url(file://" + System.getProperty("user.dir") + "/src/main/scala/style/stylesheet.css)"
  );

  // Corelates viewer with the graph
  var viewer: Viewer = graph.display()
  viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY)
  var fromViewer: ViewerPipe = viewer.newViewerPipe()
  fromViewer.addViewerListener(this)
  fromViewer.addSink(graph)
  
  // Creating world and initializing it
  // for (iter <- 0.until(25)) world.iterationLoop()
  val nodes: ListBuffer[Node] = new ListBuffer[Node]()
  val edges: ListBuffer[Edge] = new ListBuffer[Edge]()
  initializeGraph()
  Thread.sleep(2000)  // Take a short nap, the graph is stabilizing

  // Main loop which updates the world, and repaints it
  while (loop) {
    fromViewer.pump()
    world.iterationLoop()
    repaintGraph()
    Thread.sleep(Params.REFRESH_SPEED)
  }

  // Method which updates the ilustrated graph
  private def repaintGraph() {
    world.population.foreach(p => {
      graph.getNode(s"p${p.id}").setAttribute("ui.class", short(p.getState()))
    })
  }

  private def initializeGraph() {
    import AgeObject.getWorkType
    // Initialize each of the building
    world.homes.foreach(h => {
      nodes += graph.addNode(s"h${h.id}")
      nodes.last.setAttribute("ui.class", "home")
      nodes.last.setAttribute("ui.label", s"Home: ${h.id}")
    })
    world.works.foreach(w => {
      nodes += graph.addNode(s"w${w.id}")
      nodes.last.setAttribute("ui.class", "work")
      nodes.last.setAttribute("ui.label", s"Work: ${w.id}")
    })
    world.schools.foreach(s => {
      nodes += graph.addNode(s"s${s.id}")
      nodes.last.setAttribute("ui.class", "school")
      nodes.last.setAttribute("ui.label", s"School: ${s.id}")
    })

    // Initialize Citizens, and conect them with the buildings
    world.population.foreach(p => {
      nodes += graph.addNode(s"p${p.id}")
      nodes.last.setAttribute("ui.class", short(p.getState()))
      nodes.last.setAttribute("ui.label", short(p.getState()))

      // Conect nodes
      edges += graph.addEdge(s"ph${p.id}->${p.home.id}", s"p${p.id}", s"h${p.home.id}")
      getWorkType(p.age) match {
        case "work" => 
          edges += graph.addEdge(s"pw${p.id}->${p.work.id}", s"p${p.id}", s"w${p.work.id}")
        case "school" =>
          edges += graph.addEdge(s"ps${p.id}->${p.work.id}", s"p${p.id}", s"s${p.work.id}")
        case _ =>
      }
    })
  }

  override def viewClosed(x$1: String): Unit = {
    println("Closing...")
    loop = false
  }

  override def buttonPushed(id: String): Unit = {
    println("Button pushed on node " + id)
  }

  override def buttonReleased(id: String): Unit = {
    println("Button released on node " + id)
  }

  override def mouseOver(id: String): Unit = {
    println("Need the Mouse Options to be activated")
  }

  override def mouseLeft(x$1: String): Unit = {
    println("Need the Mouse Options to be activated")
  }

  
}