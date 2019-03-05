package machine.encode

import machine.standard.Table

trait DescriptionNumber[T] {
  def encode(x: T): String
}

object DescriptionNumberInstance {

  private val helper: Map[Char, Int] = Map(
    'A' -> 1,
    'C' -> 2,
    'D' -> 3,
    'L' -> 4,
    'R' -> 5,
    'N' -> 6,
    ';' -> 7
  )

  implicit def tableDescriptionNumber(implicit sd: StandardDescription[Table]): DescriptionNumber[Table] = {
    (t: Table) => sd.encode(t).map(c => helper(c)).mkString
  }
}