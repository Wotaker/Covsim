import AgeObject._
import StateSIR._
import Params._

class Citizen(
  val id: Int,
  val age: Int,
  val home: Home,
  val work: Building,
  val wearsMask: Boolean,
  val unemployed: Boolean,
  val responsible: Boolean
) {
  // Initialize:
  private var currentState: StateSIR = Suspectible
  private var infected: Boolean = false
  private var infectiousTimer: Int = 0
  private var exposedTimer: Int = 0

  def reset() {
    currentState = Suspectible
    infected = false
    infectiousTimer = 0
    exposedTimer = 0
  }

  def updateCurrentState() {
    this.currentState match {
      case Suspectible if (infected) => {
        currentState = Exposed
        exposedTimer = getIncubationTime()
      }
      case Exposed if (exposedTimer == 0) => {
        currentState = Infectious
        infectiousTimer = getInfetionTime()
      }
      case Exposed => 
        exposedTimer -= 1
      case Infectious if (infectiousTimer == 0) => {
        currentState = Recovered
        infected = false
      }
      case Infectious => {
        if (RNG.nextDouble() < getFatalityChance(age)) currentState = Dead
        else infectiousTimer -= 1
      }
      case _ => 
    }
  }

  // TODO: infect upon citizen parameters, not a constant
  def getInfectedOrNot(from: Citizen, contagionModerator: Double): Unit = {
    if (
      currentState == Suspectible && 
      (from.currentState == Exposed || (from.currentState == Infectious && !from.responsible)) &&
      RNG.nextDouble() < calcProbability(
        RAW_CONTAGION_RATE, 
        contagionModerator,
        if (wearsMask) 1 - MASK_EFFICIENCY else 1.0
      )
    ) infected = true
  }

  def calcProbability(rates: Double*): Double = rates.foldLeft(1.0)((z, x) => z * x)


  def setInfected() {
    if (currentState == Suspectible) infected = true
  }

  def isInfecting(): Boolean =
    if (currentState == Exposed || currentState == Infectious) true
    else false

  def getState(): StateSIR = currentState
  
  override def toString(): String = 
    s"Citizen ${id}, state: ${currentState.toString()}, age: ${age}, " +
    s"home: ${home.id}, ${getWorkType(age, unemployed)}: ${work.id}"
}