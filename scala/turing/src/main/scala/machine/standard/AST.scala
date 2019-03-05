package machine.standard

import machine.standard.Table.Entry
import machine.standard.{Table => STable}

// AST for standard machine table
trait AST

object AST {
  case class MConfig(i: q) extends AST
  case class Scan(mc: AST, i: S) extends AST // mc: MConfig
  case class Op(c: AST, op: Operation) extends AST // c: Scan
  case class Final(op: AST, fc: q) extends AST { // op: Op
    def toEntry: List[Entry] = this match {
      case Final(Op(Scan(MConfig(q), s), op), f) => List(Entry(q, s, op, f))
    }
  }
  case class Table(first: AST, second: AST) extends AST { // first: Final
    def toEntry: List[Entry] = first match {
      case Final(Op(Scan(MConfig(q), s), op), f) => Entry(q, s, op, f) :: {
        second match {
          case f: Final => f.toEntry
          case t: Table => t.toEntry
        }
      }
    }
  }

  implicit class StandardFormOps(ast: Table) {
    def toStandardForm: STable = STable(ast.toEntry)
  }
}