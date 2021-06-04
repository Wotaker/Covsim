import scala.util.Random
import scala.math.{pow => pow, log => log}
import java.io.File

object Params{

  // Model Parameters:
  val INFECTION_TIME: Int = 11              // Days/Iterations
  val EXPOSURE_TIME: Int = 14               // Days/Iterations
  val FATALITY_RATE: Double = 0.1
  val RAW_CONTAGION_RATE: Double = 0.03  
  val UNEMPLOYMENT_RATE: Double = 0.06
    // According to: https://www.nature.com/articles/d41586-020-02801-8
  val MASK_WEAR_RATE: Double = 0.3          // The percentage of population wearing masks (0.5 est.)
  val MASK_EFFICIENCY: Double = 0.67        // The effectivness of mask protection (0.67 est.)
  val SOCIAL_RESPONSIBILITY: Double = 0.7   // The ratio of people who avoid social conntact when infected 
  
  
  // Implementation Parameters:
  val RNG = new Random(42)        // For seed=42, and population=20 inits epidemy from citizen 2.
  val REFRESH_SPEED: Int = 100    // In ms
  val FILE_DATA: File = new File("data\\DataSEIR.csv")
  val SAVE: Boolean = true
  val DISPLAY_GRAPH: Boolean = true


  /** Calculates the incubation time in days with the distribution function.
    * Data taken from:
    * https://www.worldometers.info/coronavirus/coronavirus-incubation-period/ and
    * https://www.ncbi.nlm.nih.gov/pmc/articles/PMC7324649/
    * 
    * function plot:
    * https://www.desmos.com/calculator/mb1xsqrkre
    *
    * @return Incubation time in days/iterations
    */
  def getIncubationTime(): Int = {
    val x = RNG.nextDouble()
    if (x >= 0.5) math.ceil(5.2 - (log((1 / x) - 1) / 0.386)).toInt
    else math.ceil(4.25 - (log((1 / (x + 0.02)) - 1.7) / 1.546)).toInt
  }

  /** Calculates the infection time in days with the distribution function.
    * Data taken from:
    * https://patient.info/news-and-features/coronavirus-how-quickly-do-covid-19-symptoms-develop-and-how-long-do-they-last
    * 
    * @return Infection time in days/iterations
    */
  def getInfetionTime(): Int = {
    val x = RNG.nextDouble()
    val result = math.ceil(10.5 - (log((1 / x) - 1) / 0.945)).toInt
    if (result < 4) 4 else result
  }
}

object StateSIR extends Enumeration {
  type StateSIR = Value
  val Suspectible = Value(0, "SUSPECTIBLE")
  val Exposed = Value(1, "EXPOSED")
  val Infectious = Value(2, "INFECTIOUS")
  val Recovered = Value(3, "RECOVERED")
  val Dead = Value(4, "DEAD")

  def short(state: StateSIR): String = state match {
    case Suspectible => "S"
    case Exposed => "E"
    case Infectious => "I"
    case Recovered => "R"
    case Dead => "D"
    case _ => ""
  }
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

  def getWorkType(age: Int, unemployed: Boolean): String =
    age match {
      case a if (a < schoolAge || a >= retiredAge || unemployed) => "home"
      case a if (a < workAge) => "school"
      case _ => "work"
    }
  
  def getFatalityChance(age: Int): Double = FATALITY_RATE / INFECTION_TIME
}
