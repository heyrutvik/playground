package machine.encode

import machine.standard.Table.Entry
import machine.standard._

trait StandardForm[T] {
  def encode(x: T): String
}

object StandardFormInstance {

  implicit val m_configStandardForm: StandardForm[q] = {
    (q: q) => "q" + q.n.toString
  }

  implicit val symbolStandardForm: StandardForm[S] = {
    (s: S) => "S" + s.n.toString
  }

  implicit def rightOpsStandardForm(implicit s: StandardForm[S]): StandardForm[R] = {
    (r: R) => s.encode(r.p) + "R"
  }

  implicit def leftOpsStandardForm(implicit s: StandardForm[S]): StandardForm[L] = {
    (l: L) => s.encode(l.p) + "L"
  }

  implicit def noneOpsStandardForm(implicit s: StandardForm[S]): StandardForm[N] = {
    (n: N) => s.encode(n.p) + "N"
  }

  implicit def entryStandardForm(
    implicit q: StandardForm[q],
    s: StandardForm[S],
    r: StandardForm[R],
    l: StandardForm[L],
    n: StandardForm[N]): StandardForm[Entry] = {
      case op @ Entry(_, _, _: R, _) => q.encode(op.m_config) + s.encode(op.symbol) + r.encode(op.operation.asInstanceOf[R]) + q.encode(op.final_m_config) + ";"
      case op @ Entry(_, _, _: L, _) => q.encode(op.m_config) + s.encode(op.symbol) + l.encode(op.operation.asInstanceOf[L]) + q.encode(op.final_m_config) + ";"
      case op @ Entry(_, _, _: N, _) => q.encode(op.m_config) + s.encode(op.symbol) + n.encode(op.operation.asInstanceOf[N]) + q.encode(op.final_m_config) + ";"
  }

  implicit def tableStandardForm(implicit e: StandardForm[Entry]): StandardForm[Table] = {
    (op: Table) => op.es.map(x => e.encode(x)).mkString
  }
}