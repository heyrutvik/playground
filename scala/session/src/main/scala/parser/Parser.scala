package parser

/**
  * A parser for things
  * Is a function from strings
  * To option of pair
  * Of things and strings
  *
  * - @heyrutvik
  *
  * Note: it's a modified version, originally written by Dr Seuss.
  *       http://www.willamette.edu/~fruehr/haskell/seuss.html
  */

case class Parser[A](p: String => Option[(A, String)]) {

  import ParserApp._

  def map[B](f: A => B): Parser[B] = Parser { s =>
    parse(this)(s) match {
      case None => None
      case Some((v, r)) => Some((f(v), r))
    }
  }

  def flatMap[B](f: A => Parser[B]): Parser[B] = Parser { s =>
    parse(this)(s) match {
      case None => None
      case Some((v, r)) => parse(f(v))(r)
    }
  }

  def orElse(that: Parser[A]): Parser[A] = Parser { s =>
    parse(this)(s) match {
      case None => parse(that)(s)
      case opt => opt
    }
  }
}

object ParserBasic {

  def unit[A](value: A): Parser[A] = Parser(s => Option((value, s)))

  def failure[A]: Parser[A] = Parser(_ => None)

  def item: Parser[Char] = Parser({
    case "" => None
    case s => Option(s(0), s.drop(1))
  })
}

object ParserApp {

  // application method which abstracted over parser
  def parse[A](p: Parser[A])(s: String): Option[(A, String)] = p.p(s)
}

object ParserDerived {

  import ParserBasic._

  def satisfy(p: Char => Boolean): Parser[Char] = {
    item flatMap (c => if(p(c)) unit(c) else failure)
  }

  val digit: Parser[Char] = satisfy(c => c.isDigit)

  val lower: Parser[Char] = satisfy(c => c.isLower)

  val upper: Parser[Char] = satisfy(c => c.isUpper)

  val letter: Parser[Char] = satisfy(c => c.isLetter)

  val alphanum: Parser[Char] = satisfy(c => c.isLetterOrDigit)

  def char(c: Char): Parser[Char] = satisfy(_ == c)

  def string(s: String): Parser[String] = {
    if (s.isEmpty) unit("")
    else {
      for {
        _ <- char(s(0))
        _ <- string(s.drop(1))
      } yield (s)
    }
  }

  def many[A](p: Parser[A]): Parser[List[A]] = many1(p) orElse unit(Nil)

  def many1[A](p: Parser[A]): Parser[List[A]] = {
    for {
      a <- p
      as <- many(p)
    } yield (a :: as)
  }

  val ident: Parser[String] = for {
    c <- letter
    cs <- many(alphanum)
  } yield ((c :: cs).mkString)

  val nat: Parser[Int] = for {
    ns <- many1(digit)
  } yield (ns.mkString.toInt)

  val space: Parser[Unit] = for {
    _ <- many(char(' '))
  } yield ()

  def token[A](p: Parser[A]): Parser[A] = for {
    _ <- space
    t <- p
    _ <- space
  } yield (t)

  val identifier: Parser[String] = token(ident)

  val natural: Parser[Int] = token(nat)

  def symbol(s: String): Parser[String] = token(string(s))
}