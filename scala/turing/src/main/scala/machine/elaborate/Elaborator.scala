package machine.elaborate

import machine.compile.Move._
import machine.compile.Symbol._
import machine.regular.Table.Entry

object Elaborator {

  // scanned symbol ss and operation string
  def operation(ss: String, op: String): String = {

    def isPrint(s: String): Boolean = s.startsWith("P") || s == "E" || s == DYNAMIC
    def isMove(s: String): Boolean = List(RIGHT, LEFT, NONE).contains(s)

    val split = op.split(",").map(_.trim).toList

    def go(split: List[String], acc: List[List[String]]): List[List[String]] = {
      split match {
        case p1 :: p2 :: rest if isPrint(p1) && isPrint(p2) => go(rest, List(p2, NONE) :: acc)
        case p :: m :: rest if isPrint(p) && isMove(m) => go(rest, List(p, m) :: acc)
        case m :: p :: rest if isMove(m) && isPrint(p) => go(rest, List(DYNAMIC, m, p, NONE) :: acc)
        case m1 :: m2 :: rest if isMove(m1) && isMove(m2) => go(rest, List(DYNAMIC, m1, DYNAMIC, m2) :: acc)
        case p :: Nil if isPrint(p) => List(p, NONE) :: acc
        case m :: Nil if isMove(m) => List(DYNAMIC, m) :: acc
        case "" :: rest => go(rest, List(DYNAMIC, NONE) :: acc)
        case Nil => acc
      }
    }

    go(split, Nil).reverse.flatten.mkString(",")
  }

  def entries(es: List[Entry])(implicit symbols: Set[String]): List[Entry] = {
    var nc: Map[String, List[String]] = es.map(e => e.name -> List(e.name)).toMap
    def go(e: Entry, ops: List[String], acc: List[Entry]): List[Entry] = {
      val names = nc(e.name)
      val fn = freshen(names, e.name)
      nc += e.name -> (fn :: names)
      def create(op: String, last: Boolean = false): List[Entry] = {
        val e1 = {
          if (acc.isEmpty && last) Entry(e.name, e.symbol, op, e.next)
          else if (acc.isEmpty) Entry(e.name, e.symbol, op, fn)
          else Entry(names.head, ANY, op, if (last) e.next else fn)
        }
        if (e1.symbol == ANY) {symbols.toList.map(s => Entry(e1.name, s, e1.operation, e1.next))}
        else List(e1)
      }
      ops match {
        case Nil => acc
        case op :: Nil => create(op, true) ::: acc
        case op :: rest => go(e, rest, create(op) ::: acc)
      }
    }
    es.flatMap(e => go(e, operation(e.symbol, e.operation).split(',').grouped(2).toList.map(_.mkString(",")), Nil).reverse)
  }
}
