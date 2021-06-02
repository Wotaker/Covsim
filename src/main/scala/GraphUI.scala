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
  var data:Data = world.data

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
  
  // Creating world reprezentation as a graph
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
  println("Out of the loop")


  // Method which updates the ilustrated graph
  def repaintGraph() {
    world.population.foreach(p => {
      graph.getNode(s"p${p.id}").setAttribute("ui.class", short(p.getState()))
      blink(s"p${p.id}", p.blink, p.infectionData)
    })
  }

  def initializeGraph() {
    import AgeObject.getWorkType
    // Initialize each of the building
    world.homes.foreach(h => {
      nodes += graph.addNode(s"h${h.id}")
      nodes.last.setAttribute("ui.class", "home")
      //nodes.last.setAttribute("ui.label", s"Home: ${h.id}")
    })
    world.works.foreach(w => {
      nodes += graph.addNode(s"w${w.id}")
      nodes.last.setAttribute("ui.class", "work")
      //nodes.last.setAttribute("ui.label", s"Work: ${w.id}")
      //nodes.last.setAttribute("ui.class", "inactive")
    })
    world.schools.foreach(s => {
      nodes += graph.addNode(s"s${s.id}")
      nodes.last.setAttribute("ui.class", "school")
      //nodes.last.setAttribute("ui.label", s"School: ${s.id}")
      //nodes.last.setAttribute("ui.class", "inactive")
    })

    // Initialize Citizens, and conect them with the buildings
    world.population.foreach(p => {
      nodes += graph.addNode(s"p${p.id}")
      nodes.last.setAttribute("ui.class", short(p.getState()))
      //nodes.last.setAttribute("ui.label", short(p.getState()))

      // Conect nodes
      edges += graph.addEdge(s"${p.id}h", s"p${p.id}", s"h${p.home.id}")
      edges.last.setAttribute("ui.class", "inactive")
      getWorkType(p.age, p.unemployed) match {
        case "work" => {
          edges += graph.addEdge(s"${p.id}w", s"p${p.id}", s"w${p.work.id}")
          edges.last.setAttribute("ui.class", "inactive")
        }
        case "school" =>
        {
          edges += graph.addEdge(s"${p.id}s", s"p${p.id}", s"s${p.work.id}")
          edges.last.setAttribute("ui.class", "inactive")
        }

        case _ =>
      }
    })
  }

  override def viewClosed(x$1: String): Unit = {
    if (Params.SAVE) {
      println("Closing...")
      data.closeFile()
    }
    loop = false
  }

  private def blink(node: String, signal: Boolean, data: Option[(String, String)]){
    if (data.isDefined) {
      val superEdge: (Edge, Edge) = (
      graph.getEdge(s"${node.tail}${data.get._2.head}"),        // p7 -> h1 results in 7h edge
      graph.getEdge(s"${data.get._1.tail}${data.get._2.head}")  // p6 -> h1 results in 6h edge
      )

      if (signal) {
        superEdge._1.setAttribute("ui.class", "active")
        superEdge._2.setAttribute("ui.class", "active")
      } else {
        superEdge._1.setAttribute("ui.class", "inactive")
        superEdge._2.setAttribute("ui.class", "inactive")
      }
    }
  }

  def buttonPushed(id: String): Unit = {
    println("Button pushed on node " + id)
  }

  def buttonReleased(id: String): Unit = {
    println("Button released on node " + id)
  }

  def mouseOver(id: String): Unit = {
    println("Need the Mouse Options to be activated")
  }

  def mouseLeft(x$1: String): Unit = {
    println("Need the Mouse Options to be activated")
  }

  
}