

class Home(
  override val id: Int
) extends Building(id) {
  val letter:Char='h'
}

object Home {
  val contagionRate: Double = 0.8
}