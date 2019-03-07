package machine.interpret

import eu.timepit.refined.auto._
import machine.standard.Table.Entry
import machine.standard.{Table => STable, _}

import scala.collection.mutable.ArrayBuffer

object Interpreter {

  case class Config(mc: Q, s: S)
  case class Behaviour(o: Operation, fc: Q)
  case class CompleteConfig(state: Q, head: Int, tape: ArrayBuffer[S])

  def update(cc: CompleteConfig)(implicit context: Map[Config, Behaviour]): CompleteConfig = {
    import cc._
    val Behaviour(op, fc) = context(Config(state, tape(head)))
    val (h, t) = op match {
      case R(s) => tape(head) = if (s == S(4)) tape(head) else s; (head + 1, if (tape.size <= head + 1) tape += S(0) else tape)
      case L(s) => tape(head) = if (s == S(4)) tape(head) else s; (head - 1, tape)
      case N(s) => tape(head) = if (s == S(4)) tape(head) else s; (head, tape)
    }
    CompleteConfig(fc, h, t)
  }

  def run(step: Int, debug: Boolean = false)(implicit ct: STable, print: CompleteConfig => Unit = _ => ()): ArrayBuffer[S] = {
    implicit val context: Map[Config, Behaviour] = ct.es.map {
      case Entry(name, symbol, operation, next) => Config(name, symbol) -> Behaviour(operation, next)
    }.toMap
    if (debug) {
      List(
        "\n",
        "Configuration Context",
        "---------------",
        context.mkString("\n"),
        "---------------").foreach(println)
    }
    var cc = CompleteConfig(ct.es.head.name, 0, ArrayBuffer(S(0)))
    (1 until step).foreach { _ => print(cc); cc = update(cc)}
    cc.tape
  }
}