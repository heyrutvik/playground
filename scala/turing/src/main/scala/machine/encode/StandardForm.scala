package machine.encode

import machine.standard.Table.Entry
import machine.standard._
import machine.compile.Move._

trait StandardForm[T] {
  def encode(x: T): String
}

object StandardFormInstance {

  implicit val m_configStandardForm: StandardForm[Q] = {
    (q: Q) => "q" + q.n.toString
  }

  implicit val symbolStandardForm: StandardForm[S] = {
    (s: S) => "S" + s.n.toString
  }

  implicit def rightOpsStandardForm(implicit s: StandardForm[S]): StandardForm[R] = {
    (r: R) => s.encode(r.p) + RIGHT
  }

  implicit def leftOpsStandardForm(implicit s: StandardForm[S]): StandardForm[L] = {
    (l: L) => s.encode(l.p) + LEFT
  }

  implicit def noneOpsStandardForm(implicit s: StandardForm[S]): StandardForm[N] = {
    (n: N) => s.encode(n.p) + NONE
  }

  implicit def entryStandardForm(
                                  implicit q: StandardForm[Q],
                                  s: StandardForm[S],
                                  r: StandardForm[R],
                                  l: StandardForm[L],
                                  n: StandardForm[N]): StandardForm[Entry] = {
      case op @ Entry(_, _, _: R, _) => q.encode(op.name) + s.encode(op.symbol) + r.encode(op.operation.asInstanceOf[R]) + q.encode(op.next) + ";"
      case op @ Entry(_, _, _: L, _) => q.encode(op.name) + s.encode(op.symbol) + l.encode(op.operation.asInstanceOf[L]) + q.encode(op.next) + ";"
      case op @ Entry(_, _, _: N, _) => q.encode(op.name) + s.encode(op.symbol) + n.encode(op.operation.asInstanceOf[N]) + q.encode(op.next) + ";"
  }

  implicit def tableStandardForm(implicit e: StandardForm[Entry]): StandardForm[Table] = {
    (op: Table) => op.es.map(x => e.encode(x)).mkString
  }
}