package machine

import eu.timepit.refined.auto._
import eu.timepit.refined.api.Refined
import machine.standard.{S, q}

package object compile {

  type Context[A] = Map[String, A]
  type ConfigContext = Context[q]
  type SymbolContext = Context[S]

  object Symbol {
    val BLANK: String = "BLANK"
    val ANY: String = "ANY"
  }

  object Move {
    val RIGHT = "R"
    val LEFT = "L"
    val NONE = "N"
  }

  implicit val cc: ConfigContext = Map()
  implicit val sc: SymbolContext = Map(Symbol.BLANK -> S(0), "0" -> S(1), "1" -> S(2), Symbol.ANY -> S(3))

  def lookup[A](c: Context[A], k: String): Option[A] = c.get(k)
  def extend[A](c: Context[A], k: String, v: A): Context[A] = c.updated(k, v)

  /**
    * creates fresh m-config
    * index starts at 1
    * so, q(1), q(2), q(3) and so on
    */
  def freshMConfig(): () => q = {
    var index = 0
    () => {
      index += 1
      q(Refined.unsafeApply(index))
    }
  }

  /**
    * creates fresh symbol
    * index starts at 4
    * so, S(4), S(5), S(6) and so on
    * because, S(0) = None, S(1) = 0, S(2) = 1, S(3) = Any
    */
  def freshSymbol(): () => S = {
    var index = 3
    () => {
      index += 1
      S(Refined.unsafeApply(index))
    }
  }
}
