package parser

import ParserApp._
import ParserBasic._
import ParserDerived._

object Arithmetic extends App {
  import Grammar._

  def eval(s: String): Int = {
    parse(expr)(s) match {
      case Some((n, "")) => n
      case Some((_, out)) => sys.error(s"unconsumed input: $out")
      case None => sys.error(s"invalid input: $s")
    }
  }
}

object Grammar {

  /**
    * Parse Grammar
    *
    * expr ::= term (+ expr | @)
    * term ::= factor (* term | @)
    * factor ::= (expr) | nat
    * nat ::= 0 | 1 | 2 | ...
    *
    */

  def expr: Parser[Int] = for {
    t <- term
    repeat = symbol("+").flatMap(_ => expr.flatMap(e => unit(t + e)))
    v <- repeat.orElse(unit(t))
  } yield (v)

  def term: Parser[Int] = for {
    f <- factor
    repeat = symbol("*").flatMap(_ => term.flatMap(t => unit(f * t)))
    v <- repeat.orElse(unit(f))
  } yield (v)

  def factor: Parser[Int] = {
    val paren: Parser[Int] = for {
      _ <- symbol("(")
      e <- expr
      _ <- symbol(")")
    } yield (e)
    paren.orElse(natural)
  }
}
