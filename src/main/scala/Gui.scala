import scala.swing._
import java.io._
import java.net._
import java.time._
import org.graphstream.graph._
import org.graphstream.graph.implementations._
import scala.collection.mutable.ListBuffer
import StateSIR._
object Gui {
  var graph:SingleGraph = new SingleGraph("MainGraph");
  def main() {
    System.setProperty("org.graphstream.ui", "swing"); 
    graph.setAttribute("ui.stylesheet", "url(file://"+System.getProperty("user.dir")+"/src/main/scala/style/stylesheet.css)");
    graph.display();
  }
  def addBuildings(str:String,num:Int){
    /*
    dodaje [num] budynków typu [str]
    domy mają oznaczenie H#, prace W#, szkoły S# np. H1,W12,S7
    */
    for (i <- 0 to num){
      val n:Node =graph.addNode(str+i.toString)   
      n.setAttribute("ui.class", "building, "+str);
    }
     
  }
  def addCitizen(str:String,home:Int = -1,work:Int = -1){
      
      /*
      dodaje obywatela o [str] z domem nr [home] i pracą nr [work]; dodaje też odpowiednie krawędzie oznaczane np. EH1,EW12
      krawędzie do szkoły/pracy są nieaktywne
      */
      val n:Node= graph.addNode(str)
      n.setAttribute("ui.class", "citizen");
      if(home != -1){
        val e:Edge = graph.addEdge("EH"+str,str,"H"+home)
        blink("EH"+str)
        //e.setAttribute("ui.class","inactive");
      }
      if(work != -1){
        val e:Edge = graph.addEdge("EW"+str,str,"W"+work)
        e.setAttribute("ui.class","inactive");
      }
  }
  def setState(ctz:String,state:StateSIR){
      /*
      ustawia stan wierzchołka, nadając mu klasę css
      */
      var s:String = "";
      state match {
      case Infectious => s="infected"
      case Exposed => s="exposed"
      case Suspectible => s="suspectible"
      case Recovered => s="recovered"
      case Dead => s="dead"
      case _   => println(">>??")
    }
    val n:Node= graph.getNode(ctz)
    n.setAttribute("ui.class", s);

    // if(src!=null){
    //   val e:Edge = graph.getEdge(src)
    //   e.setAttribute("ui.class","infection");
    // }
  }
  def blink(str:String){
    /*
    ustawia klasę krawędzi [str] na "infection" na krótką chwilę 
    */
    val e:Edge = graph.getEdge(str);
    //val temp:String = e.getAttribute("ui.class");
    e.setAttribute("ui.class","infection")
    Thread.sleep(300)
    e.setAttribute("ui.class","")
    println("blink!")
  }
}