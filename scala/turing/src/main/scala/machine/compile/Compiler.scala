package machine.compile

import machine.regular.DSL
import machine.regular.DSL.{Table => DTable, _}
import machine.standard.AST
import machine.standard.AST.{Table => STable, _}
import machine.standard._

object Compiler extends App {
  def standardForm(t: DSL)(implicit cc: ConfigContext, sc: SymbolContext): (ConfigContext, SymbolContext, AST) = {

    def formatOp(ss: String, op: String): String = {
      op.split(",").map(_.trim).toList match {
        case ls @ p :: "R" :: Nil if p.startsWith("P") => ls.mkString(",")
        case ls @ p :: "L" :: Nil if p.startsWith("P") => ls.mkString(",")
        case ls @ ("R" :: Nil | "L" :: Nil) => s"""P$ss,${ls.mkString(",")}"""
        case p :: Nil if p.startsWith("P") => s"$p,N"
      }
    }

    t match {
      case Define(s) => {
        lookup(cc, s).map(c => (cc, sc, MConfig(c))).getOrElse {
          val ecc = extend(cc, s, freshMConfig())
          (ecc, sc, MConfig(lookup(ecc, s).get))
        }
      }
      case Read(mc, s) => {
        val (ecc, esc, ast) = standardForm(mc)
        lookup(esc, s).map(sym => (ecc, esc, Scan(ast, sym))).getOrElse {
          val eesc = extend(esc, s, freshSymbol())
          (ecc, eesc, Scan(ast, lookup(eesc, s).get))
        }
      }
      case Perform(c, op) => {
        val (ecc, esc, ast) = standardForm(c)
        formatOp(c.sym, op).split(",").toList match {
          case p :: "R" :: Nil => lookup(esc, p.drop(1)).map(sym => (ecc, esc, Op(ast, R(sym)))).getOrElse {
            val eesc = extend(esc, p.drop(1), freshSymbol())
            (ecc, eesc, Op(ast, R(lookup(eesc, p.drop(1)).get)))
          }
          case p :: "L" :: Nil => lookup(esc, p.drop(1)).map(sym => (ecc, esc, Op(ast, L(sym)))).getOrElse {
            val eesc = extend(esc, p.drop(1), freshSymbol())
            (ecc, eesc, Op(ast, L(lookup(eesc, p.drop(1)).get)))
          }
          case p :: "N" :: Nil => lookup(esc, p.drop(1)).map(sym => (ecc, esc, Op(ast, N(sym)))).getOrElse {
            val eesc = extend(esc, p.drop(1), freshSymbol())
            (ecc, eesc, Op(ast, N(lookup(eesc, p.drop(1)).get)))
          }
        }
      }
      case Goto(p, fc) => {
        val (ecc, esc, ast) = standardForm(p)
        lookup(ecc, fc).map(c => (ecc, esc, Final(ast, c))).getOrElse {
          val eecc = extend(ecc, fc, freshMConfig())
          (eecc, esc, Final(ast, lookup(eecc, fc).get))
        }
      }
      case DTable(goto, dsl) => {
        val (ecc, esc, ast1) = standardForm(goto)
        val (eecc, eesc, ast2) = standardForm(dsl)(ecc, esc)
        (eecc, eesc, STable(ast1, ast2))
      }
    }
  }
}
