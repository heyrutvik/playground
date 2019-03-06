package machine.elaborate

import machine.regular.Table.Entry
import machine.regular.DSL._
import machine.compile.Move._

object Elaborator {

  // scanned symbol ss and operation string
  def operation(ss: String, op: String): String = {

    val printSame = s"P$ss"

    def isPrint(s: String): Boolean = s.startsWith("P") || s == "E"
    def isMove(s: String): Boolean = List(RIGHT, LEFT, NONE).contains(s)

    val split = op.split(",").map(_.trim).toList

    def go(split: List[String], acc: List[List[String]]): List[List[String]] = split match {
      case p1 :: p2 :: rest if isPrint(p1) && isPrint(p2) => go(rest, List(p2, NONE) :: acc)
      case p :: m :: rest if isPrint(p) && isMove(m) => go(rest, List(p, m) :: acc)
      case m :: p :: rest if isMove(m) && isPrint(p) => go(rest, List(printSame, m, p, NONE) :: acc)
      case m1 :: m2 :: rest if isMove(m1) && isMove(m2) => go(rest, List(printSame, m1, printSame, m2) :: acc)
      case p :: Nil if isPrint(p) => List(p, NONE) :: acc
      case m :: Nil if isMove(m) => List(printSame, m) :: acc
      case "" :: rest => go(rest, List(printSame, NONE) :: acc)
      case Nil => acc
    }

    go(split, Nil).reverse.flatten.mkString(",")
  }

  // elaborate operation and split single entry in multiple entries
  def entry(e: Entry): List[Entry] = {
    val grouped = operation(e.symbol, e.operation).split(',').grouped(2).toList.map(_.mkString(","))
    def go(ops: List[String], names: List[String], acc: List[Entry]): List[Entry] = {
      val fn = freshen(names, e.name)
      def create(op: String, last: Boolean = false) = {
        if (acc.isEmpty && last) Entry(e.name, e.symbol, op, e.next)
        else if (acc.isEmpty) Entry(e.name, e.symbol, op, fn)
        else Entry(names.head, any, op, if (last) e.next else fn)
      }
      ops match {
        case Nil => acc
        case op :: Nil => create(op, true) :: acc
        case op :: rest => go(rest, fn :: names, create(op) :: acc)
      }
    }
    go(grouped, List(e.name), Nil).reverse
  }
}
