import Params._
import AgeObject._
import scala.collection.mutable.ListBuffer

class World(
  val populationSize: Int,
  val populationZeroSize: Int,
  val buildings: (Int, Int, Int)  // (Homes, Works, Schools)
) {
  // Initial Attributes:
  var iteration: Int = 0
  val homes: List[Home] = List.tabulate(buildings._1)(h => new Home(h + 1))
  val works: List[Work] = List.tabulate(buildings._2)(w => new Work(w + 1))
  val schools: List[School] = List.tabulate(buildings._3)(s => new School(s + 1))
  val population: List[Citizen] = generatePopulation()
  initEpidemy()
  

  // Main Loop
  def iterationLoop(): Unit = {
    iteration += 1

    // Spread infection in buildings
    homes.foreach(h => h.spreadInfection(Home.contagionRate))
    works.foreach(w => w.spreadInfection(Work.contagionRate))
    schools.foreach(s => s.spreadInfection(School.contagionRate))

    // Update states of the population
    population.foreach(p => p.updateCurrentState())
  }


  def initEpidemy() {
    RNG.shuffle(population).take(
      math.min(populationZeroSize, population.size)
    ).foreach(p => {
      p.setInfected()
      p.updateCurrentState()
    })
  }

  private def generatePopulation(): List[Citizen] = 
    List.tabulate(populationSize)(p => spawnCitizen(p + 1))

  private def spawnCitizen(citId: Int): Citizen = {
    val age = getAge()
    val home = homes(RNG.nextInt(buildings._1))
    val work = age match {
      case a if (a < schoolAge || a >= retiredAge) => home
      case a if (a < workAge) => schools(RNG.nextInt(buildings._3))
      case _ => works(RNG.nextInt(buildings._2))
    }
    val citizen = new Citizen(citId, age, home, work)
    home.inhabitants += citizen
    if (work ne home) work.inhabitants += citizen
    citizen
  }

  def printPopulation() = for (p <- population) println(p.toString())
  def printBuildings() = {
    println("===  Homes  ===\n")
    homes.foreach(h => {
      println(s"  Home ${h.id}:")
      h.printInhabitants()
    })
    println("\n===  Works  ===\n")
    works.foreach(w => {
      println(s"  Work ${w.id}:")
      w.printInhabitants()
    })
    println("\n=== Schools ===\n")
    schools.foreach(s => {
      println(s"  School ${s.id}")
      s.printInhabitants()
    })
    println("\n===============\n")
  }
}