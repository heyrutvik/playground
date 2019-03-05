package machine

import eu.timepit.refined.auto._
import eu.timepit.refined.api.Refined
import machine.standard.{S, q}

package object compile {

  type Context[A] = Map[String, A]
  type ConfigContext = Context[q]
  type SymbolContext = Context[S]

  implicit val cc: ConfigContext = Map()

  private val None: String = ""
  implicit val sc: SymbolContext = Map(None -> S(0), "0" -> S(1), "1" -> S(2))

  def lookup[A](c: Context[A], k: String): Option[A] = c.get(k)
  def extend[A](c: Context[A], k: String, v: A): Context[A] = c.updated(k, v)

  /**
    * creates fresh m-config
    * index starts at 1
    * so, q(1), q(2), q(3) and so on
    */
  val freshMConfig: () => () => q = () => {
    var index = 0
    () => {
      index += 1
      q(Refined.unsafeApply(index))
    }
  }

  /**
    * creates fresh symbol
    * index starts at 3
    * so, S(3), S(4), S(5) and so on
    * because, S(0) = None, S(1) = 0, S(2) = 1
    */
  val freshSymbol: () => () => S = () => {
    var index = 2
    () => {
      index += 1
      S(Refined.unsafeApply(index))
    }
  }
}
