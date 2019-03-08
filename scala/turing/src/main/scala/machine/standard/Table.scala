package machine.standard

import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric.{NonNegative, Positive}
import machine.standard.Table.Entry

case class Table(es: List[Entry])

object Table {
  case class Entry(mc: C, sym: S, op: O, fc: C)

}

/**
  * m-configuration
  *
  * @param n index
  */
case class C(n: Int Refined Positive)

/**
  * symbol
  *
  * @param n index
  */
case class S(n: Int Refined NonNegative)

/**
  * operation
  *
  * print Symbol and move {Right/Left/None}
  */
trait O { val p: S }

object O {
  case class R(p: S) extends O
  case class L(p: S) extends O
  case class N(p: S) extends O
}