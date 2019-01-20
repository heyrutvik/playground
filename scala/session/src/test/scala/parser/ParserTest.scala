package parser

import ParserApp._
import ParserBasic._
import ParserDerived._
import common.UnitTest

class ParserTest extends UnitTest("Main") {

  "basic combinator" should "parse" in {
    parse(unit(1))("abc") should be (Option(1, "abc"))
    parse(failure)("abc") should be (None)
    parse(item)("abc") should be (Option('a', "bc"))
    parse(item)("") should be (None)
  }

  "sequencing" should "parse first and third character" in {
    val p: Parser[(Char, Char)] = {
      for {
        a <- item
        _ <- item
        c <- item
      } yield (a, c)
    }
    parse(p)("abcde") should be (Option(('a', 'c'), "de"))
    parse(p)("abc") should be (Option(('a', 'c'), ""))
    parse(p)("ab") should be (None)
  }

  "choice" should "parse second if first fails" in {
    parse(item.orElse(unit('d')))("") should be (Option('d', ""))
    parse(item.orElse(unit('d')))("abc") should be (Option('a', "bc"))
    parse(failure.orElse(unit('d')))("abc") should be (Option('d', "abc"))
    parse(failure.orElse(failure))("abc") should be (None)
  }

  "derived combinator" should "parse" in {
    parse(many(digit))("123abc") should be (Option(List('1','2','3'), "abc"))
    parse(many(digit))("") should be (Option(Nil, ""))
    parse(many1(lower))("a") should be (Option(List('a'), ""))
    parse(many1(lower))("") should be (None)

    parse(ident)("flag1") should be (Option("flag1", ""))
    parse(ident)("") should be (None)
    parse(nat)("123abc") should be (Option(123, "abc"))
    parse(nat)("abc") should be (None)
    parse(space)("  abc") should be (Option((), "abc"))

    parse(token(ident))("  abc      ") should be (Option("abc", ""))
    parse(token(nat))("  123   345   ") should be (Option(123, "345   "))

    parse(identifier)("  abc      ") should be (Option("abc", ""))
    parse(natural)("  123   345   ") should be (Option(123, "345   "))

    parse(symbol("+"))("  +      ") should be (Option("+", ""))
    parse(symbol("-"))("-") should be (Option("-", ""))
    parse(symbol("-"))("") should be (None)

    val jsArrayParser: Parser[List[Int]] = for {
      _ <- symbol("[")
      n <- natural
      ns <- many(symbol(",").flatMap(_ => natural))
      _ <- symbol("]")
    } yield (n :: ns)

    val jsArrayParser1: Parser[List[Int]] = {
      symbol("[")
        .flatMap(_ => natural
          .flatMap(n => many(symbol(",").flatMap(_ => natural))
            .flatMap(ns => symbol("]")
              .flatMap(_ => unit(n :: ns)))))
    }

    parse(jsArrayParser1)("[  ]") should be (None)
    parse(jsArrayParser1)("[1]") should be (Option(List(1), ""))
    parse(jsArrayParser1)("[1,]") should be (None)
    parse(jsArrayParser1)("[ 1, 2  , 3]") should be (Option(List(1,2,3), ""))
  }
}