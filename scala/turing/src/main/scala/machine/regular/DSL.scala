package machine.regular

import machine.regular.DSL.{Define, Goto, Perform, Read}
import machine.regular.Table.Entry

// this serves two purposes
// DSL to write machine table
// AST to compile down to regular form
trait DSL

object DSL extends DSL0 {
  val blank = "BLANK"
  val any = "ANY"
  case class Define(s: String) extends DSL
  case class Read(mc: Define, sym: String) extends DSL
  case class Perform(c: Read, op: String) extends DSL
  case class Goto(op: Perform, fc: String) extends DSL {
    def |:(ls: List[Goto]): List[Goto] = ls ::: this
  }
  case class Table(first: Goto, second: DSL) extends DSL

  implicit class DefineOps(mc: String) {
    def read(sym: String): Read = Read(Define(mc), sym)
  }
  implicit class ReadOps(c: Read) {
    def perform(op: String): Perform = Perform(c, op)
  }
  implicit class PerformOps(op: Perform) {
    def goto(fc: String): Goto = Goto(op, fc)
  }
  implicit class GotoOps(ls: List[Goto] = List.empty) {
    def |:(l: Goto): List[Goto] = l :: ls
  }
  implicit def mkList(l: Goto): List[Goto] = List(l)
  implicit def mkEntry(ls: List[Goto]): List[Entry] = ls.map {
    case Goto(Perform(Read(Define(mc), sym), op), fc) => Entry(mc, sym, op, fc)
  }
}

// to resolve the conflict of `DSL.mkList` and `DSL0.singleEntry`
trait DSL0 {
  implicit def moreEntry(e: Goto): List[Entry] = List(singleEntry(e))
  implicit def singleEntry(e: Goto): Entry = e match {
    case Goto(Perform(Read(Define(mc), sym), op), fc) => Entry(mc, sym, op, fc)
  }
}