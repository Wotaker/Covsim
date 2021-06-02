

class Work(
  override val id: Int
) extends Building(id) {
  val letter: Char = 'w'
}

object Work {
  val contagionRate: Double = 0.4
}