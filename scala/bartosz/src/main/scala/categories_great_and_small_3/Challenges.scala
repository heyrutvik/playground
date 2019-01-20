package categories_great_and_small_3

object Challenges {

  // addition modulo 3 as a monoid category
  val modulo3monoid = MonoidInstance.modulo3
}

trait Monoid[A] {
  def mempty: A
  def mappend: A => A => A
}

case class Modulo3(value: Int)

object MonoidInstance {

  implicit val modulo3 = new Monoid[Modulo3] {
    override def mempty: Modulo3 = Modulo3(3)
    override def mappend: Modulo3 => Modulo3 => Modulo3 = m1 => m2 => {
      Modulo3((m1.value + m2.value) % mempty.value)
    }
  }
}