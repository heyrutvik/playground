package machine.interpret

import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.NonNegative
import machine.standard.O._
import machine.standard.Table.Entry
import machine.standard.{Table => STable, _}

import scala.collection.mutable.ArrayBuffer

object Interpreter {

  case class Config(mc: C, s: S)
  case class Behaviour(o: O, fc: C)
  case class CompleteConfig(state: C, head: Int, tape: ArrayBuffer[S])

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

  def run(step: Int Refined NonNegative, debug: Boolean = false)(implicit ct: STable, print: CompleteConfig => Unit = _ => ()): ArrayBuffer[S] = {
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
    var cc = CompleteConfig(ct.es.head.mc, 0, ArrayBuffer(S(0)))
    (if (step > 0) {
      1 until step
    } else {
      Stream.from(1)
    }).foreach { _ => print(cc); cc = update(cc)}
    cc.tape
  }
}