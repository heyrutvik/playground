package machine.regular

import machine.regular.Table.Entry

object DSL {
  case class MConfig(s: String)
  case class Configuration(mc: MConfig, sym: String)
  case class Operation(c: Configuration, op: String)
  case class Line(op: Operation, fc: String) {
    def |:(ls: List[Line]): List[Line] = ls ::: this
  }
  implicit class MConfigOps(mc: String) {
    def read(sym: String): Configuration = Configuration(MConfig(mc), sym)
  }
  implicit class SymbolOps(c: Configuration) {
    def perform(op: String): Operation = Operation(c, op)
  }
  implicit class OperationOps(op: Operation) {
    def goto(fc: String): Line = Line(op, fc)
  }
  implicit class LineOps0(ls: List[Line] = List.empty) {
    def |:(l: Line): List[Line] = l :: ls
  }
  implicit def mkEntry(ls: List[Line]): List[Entry] = ls.map {
    case Line(Operation(Configuration(MConfig(mc), sym), op), fc) => Entry(mc, sym, op, fc)
  }
  implicit def mkList(l: Line): List[Line] = List(l)
}