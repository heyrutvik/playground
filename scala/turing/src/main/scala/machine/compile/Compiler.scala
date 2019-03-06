package machine.compile

import machine.regular.DSL
import machine.regular.DSL.{Table => DTable, _}
import machine.standard.AST.{Table => STable, _}
import machine.standard.{AST, _}

case class Compiler() {

  // scanned symbol ss and operation string
  private def elaborate(ss: String, op: String): String = {

    val (mr, ml, mn) = ("R", "L", "N")
    val printSame = s"P$ss"

    def isPrint(s: String): Boolean = s.startsWith("P") || s == "E"
    def isMove(s: String): Boolean = List(mr, ml, mn).contains(s)

    val split = op.split(",").map(_.trim).toList

    def go(split: List[String], acc: List[List[String]]): List[List[String]] = split match {
      case p1 :: p2 :: rest if isPrint(p1) && isPrint(p2) => go(rest, List(p2, mn) :: acc)
      case p :: m :: rest if isPrint(p) && isMove(m) => go(rest, List(p, m) :: acc)
      case m :: p :: rest if isMove(m) && isPrint(p) => go(rest, List(printSame, m, p, mn) :: acc)
      case m1 :: m2 :: rest if isMove(m1) && isMove(m2) => go(rest, List(printSame, m1, printSame, m2) :: acc)
      case p :: Nil if isPrint(p) => List(p, mn) :: acc
      case m :: Nil if isMove(m) => List(printSame, m) :: acc
      case "" :: rest => go(rest, List(printSame, mn) :: acc)
      case Nil => acc
    }

    go(split, Nil).reverse.flatten.mkString(",")
  }

  private def _standardForm(t: DSL)(implicit freshmc: () => q, freshsym: () => S, cc: ConfigContext, sc: SymbolContext): (ConfigContext, SymbolContext, AST) = {
    t match {
      case Define(s) => {
        lookup(cc, s).map(c => (cc, sc, MConfig(c))).getOrElse {
          val ecc = extend(cc, s, freshmc())
          (ecc, sc, MConfig(lookup(ecc, s).get))
        }
      }
      case Read(mc, s) => {
        val (ecc, esc, ast) = _standardForm(mc)
        lookup(esc, s).map(sym => (ecc, esc, Scan(ast, sym))).getOrElse {
          val eesc = extend(esc, s, freshsym())
          (ecc, eesc, Scan(ast, lookup(eesc, s).get))
        }
      }
      case Perform(c, op) => {
        val (ecc, esc, ast) = _standardForm(c)
        elaborate(c.sym, op).split(",").toList match {
          case "E" :: (m @ ("R" | "L" | "N")) :: Nil =>
            (ecc, esc, Op(ast, R(lookup(esc, "").get)))
          case p :: "R" :: Nil => lookup(esc, p.drop(1)).map(sym => (ecc, esc, Op(ast, R(sym)))).getOrElse {
            val eesc = extend(esc, p.drop(1), freshsym())
            (ecc, eesc, Op(ast, R(lookup(eesc, p.drop(1)).get)))
          }
          case p :: "L" :: Nil => lookup(esc, p.drop(1)).map(sym => (ecc, esc, Op(ast, L(sym)))).getOrElse {
            val eesc = extend(esc, p.drop(1), freshsym())
            (ecc, eesc, Op(ast, L(lookup(eesc, p.drop(1)).get)))
          }
          case p :: "N" :: Nil => lookup(esc, p.drop(1)).map(sym => (ecc, esc, Op(ast, N(sym)))).getOrElse {
            val eesc = extend(esc, p.drop(1), freshsym())
            (ecc, eesc, Op(ast, N(lookup(eesc, p.drop(1)).get)))
          }
        }
      }
      case Goto(p, fc) => {
        val (ecc, esc, ast) = _standardForm(p)
        lookup(ecc, fc).map(c => (ecc, esc, Final(ast, c))).getOrElse {
          val eecc = extend(ecc, fc, freshmc())
          (eecc, esc, Final(ast, lookup(eecc, fc).get))
        }
      }
      case DTable(goto, dsl) => {
        val (ecc, esc, ast1) = _standardForm(goto)
        val (eecc, eesc, ast2) = _standardForm(dsl)(freshmc, freshsym, ecc, esc)
        (eecc, eesc, STable(ast1, ast2))
      }
    }
  }

  def standardForm(t: DSL, debug: Boolean = false): AST = {
    implicit val freshmc = freshMConfig()
    implicit val freshsym = freshSymbol()
    val (x, y, ast) = _standardForm(t)
    if (debug) {
      List("Config Context", "---------------", x.mkString("\n")).foreach(println)
      List("Symbol Context", "---------------", y.mkString("\n")).foreach(println)
    }
    ast
  }
}
