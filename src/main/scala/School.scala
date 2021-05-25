

class School(
  override val id: Int
) extends Building(id) {
  
}

object School {
  val contagionRate: Double = 0.6
}