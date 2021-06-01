

class School(
  override val id: Int
) extends Building(id) {
  val letter:Char='s'
}

object School {
  val contagionRate: Double = 0.6
}