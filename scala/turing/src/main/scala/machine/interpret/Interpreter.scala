package machine.interpret

import eu.timepit.refined.auto._
import machine.compile.{Compiler, Symbol}
import machine.regular.{Table => RTable}
import machine.standard.Table.Entry
import machine.standard._

import scala.collection.mutable.ArrayBuffer

case class Interpreter(rt: RTable) {

  private case class Config(mc: Q, s: S)
  private case class Behaviour(o: Operation, fc: Q)
  private case class CompleteConfig(state: Q, head: Int, tape: ArrayBuffer[S])

  private val (_, sc, ast) = Compiler(rt.mkDSL)
  private val cs = sc.map(_.swap)
  private val ct = ast.toStandardTable
  private val context: Map[Config, Behaviour] = ct.es.map {
    case Entry(name, symbol, operation, next) => Config(name, symbol) -> Behaviour(operation, next)
  }.toMap

  private def update(cc: CompleteConfig): CompleteConfig = {
    import cc._
    val Behaviour(op, fc) = context(Config(state, tape(head)))
    val (h, t) = op match {
      case R(s) => tape(head) = if (s == S(4)) tape(head) else s; (head + 1, if (tape.size <= head + 1) tape += S(0) else tape)
      case L(s) => tape(head) = if (s == S(4)) tape(head) else s; (head - 1, tape)
      case N(s) => tape(head) = if (s == S(4)) tape(head) else s; (head, tape)
    }
    CompleteConfig(fc, h, t)
  }

  def run(tapeSize: Int, debug: Boolean = false): String = {
    if (debug) {
      List("\n", "Configuration Context", "---------------", context.mkString("\n"), "---------------").foreach(println)
    }
    var cc = CompleteConfig(ct.es.head.name, 0, ArrayBuffer(S(0)))
    (1 until tapeSize).foreach(_ => cc = update(cc))
    cc.tape.map(c => cs(c)).map {
      case Symbol.BLANK => "_"
      case s => s
    }.toList.mkString("|", "|", "|")
  }
}

object Interpreter {
  implicit class RTableOps(rt: RTable) {
    def run(step: Int): String = Interpreter(rt).run(step)
    def runWithDebug(step: Int): String = Interpreter(rt).run(step, true)
  }
}