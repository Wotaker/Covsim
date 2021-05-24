import scala.util.Random
import scala.math.{pow => pow}

object Params{
  // Model Parameters:
  val INFECTION_TIME: Int = 5  // Days/Iterations
  val EXPOSURE_TIME: Int = 14  // Days/Iterations
  val FATALITY_RATE: Double = 0.25
  val TEMP_CONTAGION_RATE: Double = 0.05   // TODO: infect upon citizen parameters, not a constant
  
  // Implementation Parameters:
  val RNG = new Random(42)  // For seed=42, and population=20 inits epidemy from citizen 2.
}

object StateSIR extends Enumeration {
  type StateSIR = Value
  val Suspectible = Value("SUSPECTIBLE")
  val Exposed = Value("EXPOSED")
  val Infectious = Value("INFECTIOUS")
  val Recovered = Value("RECOVERED")
  val Dead = Value("DEAD")
}

object AgeObject {
  import Params._
  val schoolAge: Int = 6
  val workAge: Int = 20
  val retiredAge: Int = 67

  def getAge(): Int = {
    val x = Params.RNG.nextDouble()
    val age: Int = (
        0.1238043 + 
        74.394384 * x + 
        432.71154 * pow(x, 2) - 
        2391.0028 * pow(x, 3) +
        4952.8016 * pow(x, 4) -
        4332.5399 * pow(x, 5) +
        1079.3973 * pow(x, 6) +
        274.21116 * pow(x, 7)
    ).toInt
    age
  }

  def getWorkType(age: Int): String =
    age match {
      case a if (a < schoolAge || a >= retiredAge) => "home"
      case a if (a < workAge) => "school"
      case _ => "work"
    }
  
  def getFatalityChance(age: Int): Double = FATALITY_RATE / INFECTION_TIME
}
