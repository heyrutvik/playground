package machine.encode

import machine.compile.Move._
import machine.standard.O._
import machine.standard.Table.Entry
import machine.standard._

trait StandardForm[T] {
  def encode(x: T): String
}

object StandardFormInstance {

  implicit val m_configStandardForm: StandardForm[C] = {
    (q: C) => "q" + q.n.toString
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
                                  implicit q: StandardForm[C],
                                  s: StandardForm[S],
                                  op: StandardForm[O]): StandardForm[Entry] = {
    (e: Entry) => q.encode(e.mc) + s.encode(e.sym) + op.encode(e.op) + q.encode(e.fc) + ";"
  }

  implicit def tableStandardForm(implicit e: StandardForm[Entry]): StandardForm[Table] = {
    (op: Table) => op.es.map(x => e.encode(x)).mkString
  }
}