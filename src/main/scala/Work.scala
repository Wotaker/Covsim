

class Work(
  override val id: Int
) extends Building(id) {
  
}

object Work {
  val contagionRate: Double = 1.0
}