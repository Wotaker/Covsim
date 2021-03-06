import Params._
import AgeObject._
import StateSIR._
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
  val currentSimulationState: Array[Int] = Array(0, 0, 0, 0, 0, 0)

  val data = new Data()
  if (SAVE) data.initialize()
  initEpidemy()
  

  // Main Loop
  def iterationLoop(): Boolean = {
    iteration += 1
    if (SAVE && data.keepWriting) countStates(iteration)

    if (!DISPLAY_GRAPH && SAVE && !data.keepWriting) {
      println("Closing...")
      data.closeFile()
      false
    }
    else {
      // Spread infection in buildings
      homes.foreach(h => h.spreadInfection(Home.contagionRate, s"h${h.id}"))
      works.foreach(w => w.spreadInfection(Work.contagionRate, s"w${w.id}"))
      schools.foreach(s => s.spreadInfection(School.contagionRate, s"s${s.id}"))

      // Update states of the population
      population.foreach(p => p.updateCurrentState())
      true
    }
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
    var unemployed = false
    val work = age match {
      case a if (
        a < schoolAge || a >= retiredAge || RNG.nextDouble() < UNEMPLOYMENT_RATE
      ) => { unemployed = true; home }
      case a if (a < workAge) => schools(RNG.nextInt(buildings._3))
      case _ => works(RNG.nextInt(buildings._2))
    }
    val citizen = new Citizen(
      citId, age, home, work,
      RNG.nextDouble() < MASK_WEAR_RATE,
      unemployed,
      RNG.nextDouble() < SOCIAL_RESPONSIBILITY
    )
    home.inhabitants += citizen
    if (work ne home) work.inhabitants += citizen
    citizen
  }

  def countStates(iteration: Int) {
    currentSimulationState(0) = iteration
    population.foreach(p => currentSimulationState(p.getState().id + 1) += 1)
    data.writeData(currentSimulationState.map(n => n.toString()))
    if (currentSimulationState(2) == 0 && currentSimulationState(3) == 0) {
      data.keepWriting = false
      println("COVID-19 DEFEATED!")
    }

    for (i <- 0.until(currentSimulationState.size)) currentSimulationState(i) = 0
  }

  /** Calculates R0 from model parameters, according to
    * https://web.stanford.edu/class/earthsys214/notes/Jones_R0_notes2019.pdf equation (1)
    *
    * @return R0 - Basic Reproduction Number
    */
  def calcR0(): Double = {
    var totalContacts = 0.0
    var totalProbs = 0.0
    for (p <- population) {
      totalContacts += (p.home.inhabitants.size - 1)
      if (p.home ne p.work) totalContacts += (p.work.inhabitants.size - 1)
      totalProbs += p.calcProbContact()
    }
    val c = totalContacts / populationSize
    val t = totalProbs / populationSize
    val d = INFECTION_TIME + (EXPOSURE_TIME * (1.0 - SOCIAL_RESPONSIBILITY))
    c * t * d     // Calculated R0
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