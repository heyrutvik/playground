package turingmachine

case class Tape(left: Vector[Char], middle: Char, right: Vector[Char], blank: Char) {

  override def toString: String = s"<Tape ${left.mkString}($middle)${right.mkString}>"

  def write(char: Char) = Tape(left, char, right, blank)

  def moveHeadLeft = Tape(left.dropRight(1), left.lastOption.getOrElse(blank), middle +: right, blank)

  def moveHeadRight = Tape(left :+ middle, right.headOption.getOrElse(blank), right.drop(1), blank)
}