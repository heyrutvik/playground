package machine.encode

import machine.Table.Entry
import machine._

trait StandardDescription[T] {
  def encode(x: T): String
}

object StandardDescriptionInstance {

  implicit val mconfigStandardDescription: StandardDescription[q] = {
    (mc: q) => "D" + (1 to mc.n.value).map(_ => "A").mkString
  }

  implicit val symbolStandardDescription: StandardDescription[S] = {
    (s: S) => "D" + (1 to s.n.value).map(_ => "C").mkString
  }

  implicit def rightOpStandardDescription(implicit s: StandardDescription[S]): StandardDescription[R] = {
    (r: R) => s.encode(r.p) + "R"
  }

  implicit def leftOpStandardDescription(implicit s: StandardDescription[S]): StandardDescription[L] = {
    (l: L) => s.encode(l.p) + "L"
  }

  implicit def noneOpStandardDescription(implicit s: StandardDescription[S]): StandardDescription[N] = {
    (n: N) => s.encode(n.p) + "N"
  }

  implicit def entryStandardDescription(
    implicit q: StandardDescription[q],
    s: StandardDescription[S],
    r: StandardDescription[R],
    l: StandardDescription[L],
    n: StandardDescription[N]): StandardDescription[Entry] = {
      case op @ Entry(_, _, _: R, _) => q.encode(op.m_config) + s.encode(op.symbol) + r.encode(op.operation.asInstanceOf[R]) + q.encode(op.final_m_config) + ";"
      case op @ Entry(_, _, _: L, _) => q.encode(op.m_config) + s.encode(op.symbol) + l.encode(op.operation.asInstanceOf[L]) + q.encode(op.final_m_config) + ";"
      case op @ Entry(_, _, _: N, _) => q.encode(op.m_config) + s.encode(op.symbol) + n.encode(op.operation.asInstanceOf[N]) + q.encode(op.final_m_config) + ";"
  }

  implicit def tableStandardDescription(implicit e: StandardDescription[Entry]): StandardDescription[Table] = {
    (t: Table) => t.es.map(x => e.encode(x)).mkString
  }
}