package machine.compile

import machine.elaborate.Elaborator
import machine.regular.DSL
import machine.regular.DSL.{Table => DTable, _}
import machine.standard.AST.{Table => STable, _}
import machine.standard.{AST, _}

case class Compiler() {

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
        Elaborator.operation(c.sym, op).split(",").toList match {
          case "E" :: (m @ ("R" | "L" | "N")) :: Nil =>
            (ecc, esc, Op(ast, R(lookup(esc, BLANK).get)))
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
