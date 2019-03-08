package machine.standard

import machine.standard.Table.Entry

// AST for standard machine table
trait AST

object AST {
  case class MC(mc: C) extends AST
  case class Sy(mc: AST, s: S) extends AST // mc: MC
  case class Op(c: AST, o: O) extends AST // c: Sym
  case class FC(op: AST, fc: C) extends AST { // op: Op
    def toEntry: List[Entry] = this match {
      case FC(Op(Sy(MC(q), s), op), f) => List(Entry(q, s, op, f))
    }
  }
  case class Table(first: AST, second: AST) extends AST { // first: FC
    def toEntry: List[Entry] = first match {
      case FC(Op(Sy(MC(q), s), op), f) => Entry(q, s, op, f) :: {
        second match {
          case f: FC => f.toEntry
          case t: Table => t.toEntry
        }
      }
    }
  }
}