package machine.standard

import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric.NonNegative
import machine.encode.StandardDescription

/**
  * symbol
  *
  * @param n index
  */
case class S(n: Int Refined NonNegative)

object S {
  def sd(mc: S)(implicit ev: StandardDescription[S]): String = ev.encode(mc)
}