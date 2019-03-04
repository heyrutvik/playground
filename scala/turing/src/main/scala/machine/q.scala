package machine

import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric.Positive
import machine.encode.StandardDescription

/**
  * m-configuration
  *
  * @param n index
  */
case class q(n: Int Refined Positive)

object q {
  def sd(mc: q)(implicit ev: StandardDescription[q]): String = ev.encode(mc)
}