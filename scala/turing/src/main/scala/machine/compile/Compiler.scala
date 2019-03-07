package machine.compile

import machine.compile.Move._
import machine.elaborate.Elaborator
import machine.implicits.Instance._
import machine.regular.DSL
import machine.regular.DSL.{Table => DTable, _}
import machine.standard.AST.{Table => STable, _}
import machine.standard.{AST, _}

object Compiler {

  private def _standardForm(t: DSL)(implicit freshmc: () => Q, freshsym: () => S, cc: ConfigContext, sc: SymbolContext): (ConfigContext, SymbolContext, AST) = {
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
          case "E" :: (m @ (RIGHT | LEFT | NONE)) :: Nil =>
            (ecc, esc, Op(ast, R(lookup(esc, Symbol.BLANK).get)))
          case p :: RIGHT :: Nil => lookup(esc, p.drop(1)).map(sym => (ecc, esc, Op(ast, R(sym)))).getOrElse {
            val eesc = extend(esc, p.drop(1), freshsym())
            (ecc, eesc, Op(ast, R(lookup(eesc, p.drop(1)).get)))
          }
          case p :: LEFT :: Nil => lookup(esc, p.drop(1)).map(sym => (ecc, esc, Op(ast, L(sym)))).getOrElse {
            val eesc = extend(esc, p.drop(1), freshsym())
            (ecc, eesc, Op(ast, L(lookup(eesc, p.drop(1)).get)))
          }
          case p :: NONE :: Nil => lookup(esc, p.drop(1)).map(sym => (ecc, esc, Op(ast, N(sym)))).getOrElse {
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

  def apply(t: DSL): (ConfigContext, SymbolContext, AST) = {
    implicit val freshmc = freshMConfig()
    implicit val freshsym = freshSymbol()
    val (c, s, ast) = _standardForm(t)
    (c, s, ast)
  }

  def toAST(t: DSL, debug: Boolean = false): AST = {
    val (c, s, ast) = apply(t)
    if (debug) {
      List("\n", "Config Context", "---------------", c.mkString("\n"), "---------------").foreach(println)
      List("\n", "Symbol Context", "---------------", s.mkString("\n"), "---------------").foreach(println)
    }
    ast
  }

  def toASTWithDebug(t: DSL): AST = toAST(t, true)
}
