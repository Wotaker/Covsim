import scala.collection.mutable.ListBuffer


abstract class Building(
  val id: Int
) {
  // Initialize building:
  val letter: Char
  val inhabitants: ListBuffer[Citizen] = 
    new ListBuffer[Citizen]()

  def spreadInfection(buildingRate: Double, buildingNode: String) {
    for (i <- inhabitants) {
      if (i.isInfecting()) for (j <- inhabitants) {
        if (i ne j) j.getInfectedOrNot(i, buildingRate, buildingNode)
      }
    }
  }

  def printInhabitants() = inhabitants.foreach(p => println(s"    ${p.toString()}"))
}