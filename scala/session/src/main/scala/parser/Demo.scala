package parser

object Demo {

  // parser type
  type Parser[A] = String => Option[(A, String)]

  /**
    * basic parsers
    */

  // always succeeds
  def unit[A](value: A): Parser[A] = s => Option((value, s))

  // always fails
  def fail[A]: Parser[A] = _ => None

  // take first character from string, otherwise fails
  def item: Parser[Char] = {
    case "" => None
    case s => Option((s(0), s.drop(1)))
  }

  /**
    * function to abstract over parser functions
    */
  def parse[A](p: Parser[A])(s: String): Option[(A, String)] = p(s)

  /**
    * parser sequencing
    */
  def andThen[A, B](p: Parser[A])(f: A => Parser[B]): Parser[B] = { s =>
    parse(p)(s) match {
      case None => None
      case Some((a, r)) => parse(f(a))(r)
    }
  }

  /**
    * parser choice
    */
  def orElse[A](p1: Parser[A], p2: Parser[A]): Parser[A] = { s =>
    parse(p1)(s) match {
      case None => parse(p2)(s)
      case opt => opt
    }
  }

  /**
    * derived functions
    */
  def satisfy(p: Char => Boolean): Parser[Char] = {
    andThen(item) { c =>
      if (p(c)) unit(c) else fail
    }
  }

  val digit: Parser[Char] = satisfy(c => c.isDigit)

  val lower: Parser[Char] = satisfy(c => c.isLower)

  val upper: Parser[Char] = satisfy(c => c.isUpper)

  val letter: Parser[Char] = satisfy(c => c.isLetter)

  val alphanum: Parser[Char] = satisfy(c => c.isLetterOrDigit)

  def char(c: Char): Parser[Char] = satisfy(_ == c)

  /**
    * parse string using recursion
    */
  def string(s: String): Parser[String] = {
    if (s.isEmpty) unit("")
    else {
      andThen(char(s(0))) { _ =>
        andThen(string(s.drop(1))) { _ =>
          unit(s)
        }
      }
    }
  }


  /**
    * parse multiple times using mutual recursion
    */
  def many[A](p: Parser[A]): Parser[List[A]] = {
    orElse(many1(p), unit(Nil))
  }

  def many1[A](p: Parser[A]): Parser[List[A]] = {
    andThen(p) { v =>
      andThen(many(p)) { vs =>
        unit(v :: vs)
      }
    }
  }

  val ident: Parser[String] = {
    andThen(lower) { c =>
      andThen(many(alphanum)) { cs =>
        unit((c :: cs).mkString)
      }
    }
  }

  val nat: Parser[Int] = {
    andThen(many1(digit)) { ds =>
      unit(ds.mkString.toInt)
    }
  }

  val space: Parser[Unit] = {
    andThen(many(char(' '))) { _ =>
      unit(())
    }
  }

  def token[A](p: Parser[A]): Parser[A] = {
    andThen(space) { _ =>
      andThen(p) { v =>
        andThen(space) { _ =>
          unit(v)
        }
      }
    }
  }

  val identifier: Parser[String] = token(ident)

  val natural: Parser[Int] = token(nat)

  def symbol(s: String): Parser[String] = token(string(s))

  // example: parser for list of integer [1,2,3]
  def jsArrayParser: Parser[List[Int]] = {
    andThen(symbol("[")) { _ =>
      andThen(natural) { n =>
        andThen(many(andThen(symbol(",")) { _ => natural})) { ns =>
          andThen(symbol("]")) { _ =>
            unit(n :: ns)
          }
        }
      }
    }
  }

  /**
    * Arithmetic expression parser
    *
    * /**
    * * Parse Grammar
    * *
    * * expr ::= term (+ expr | @)
    * * term ::= factor (* term | @)
    * * factor ::= (expr) | nat
    * * nat ::= 0 | 1 | 2 | ...
    * *
    **/
    */

  def expr: Parser[Int] = {
    andThen(term) { t =>
      val repeat = andThen(symbol("+")) { _ =>
        andThen(expr) { e =>
          unit(t + e)
        }
      }
      orElse(repeat, unit(t))
    }
  }

  def term: Parser[Int] = {
    andThen(factor) { f =>
      val repeat = andThen(symbol("*")) { _ =>
        andThen(term) { t =>
          unit(f * t)
        }
      }
      orElse(repeat, unit(f))
    }
  }

  def factor: Parser[Int] = {
    val paren: Parser[Int] = {
      andThen(symbol("(")) { _ =>
        andThen(expr) { e =>
          andThen(symbol(")")) { _ =>
            unit(e)
          }
        }
      }
    }
    orElse(paren, natural)
  }

  // expression evaluator
  def eval(s: String): Int = {
    parse(expr)(s) match {
      case Some((n, "")) => n
      case Some((_, rest)) => throw new RuntimeException(s"unconsumed input: $rest")
      case _ => throw new RuntimeException(s"invalid input: $s")
    }
  }
}