

class Home(
  override val id: Int
) extends Building(id) {
  
}

object Home {
  val contagionRate: Double = 1.0
}