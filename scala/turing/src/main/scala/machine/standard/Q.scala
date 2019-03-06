package machine.standard

import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric.Positive
import machine.encode.StandardDescription

/**
  * m-configuration
  *
  * @param n index
  */
case class Q(n: Int Refined Positive)

object Q {
  def sd(mc: Q)(implicit ev: StandardDescription[Q]): String = ev.encode(mc)
}