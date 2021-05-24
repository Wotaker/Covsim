import scala.collection.mutable.ListBuffer


class Building(
  val id: Int
) {
  // Initialize building:
  val inhabitants: ListBuffer[Citizen] = 
    new ListBuffer[Citizen]()

  def spreadInfection(buildingRate: Double) {
    for (i <- inhabitants) {
      if (i.isInfecting()) for (j <- inhabitants) {
        if (i ne j) j.getInfectedOrNot(i, buildingRate)
      }
    }
  }

  def printInhabitants() = inhabitants.foreach(p => println(s"    ${p.toString()}"))
}