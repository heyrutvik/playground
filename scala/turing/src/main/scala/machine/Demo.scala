package machine

import machine.compile.Compiler
import machine.interpret.Interpreter._
import machine.regular.DSL._
import machine.regular.{Table => RTable}
import machine.standard.AST._

object Demo extends App {

  val t1 = RTable {
    {"b" read blank perform "P0, R" goto "c"} |:
    {"c" read blank perform "R"     goto "e"} |:
    {"e" read blank perform "P1, R" goto "f"} |:
    {"f" read blank perform "R"     goto "b"}
  }

  val t2 = RTable {
    {"a" read blank perform "P0, R" goto "b"} |:
    {"b" read blank perform "P1, L" goto "c"} |:
    {"c" read "0" perform "P0, R" goto "d"} |:
    {"d" read "1" perform "P1, L" goto "c"}
  }

  val t3 = RTable {
    {"b" read blank perform "Pe, R, Pe, R, P0, R, R, P0, L, L" goto "o"} |:
    {"o" read "1"   perform "R, Px, L, L, L"                   goto "o"} |:
    {"o" read "0"   perform ""                                 goto "q"} |:
    {"q" read "0"   perform "R, R"                             goto "q"} |:
    {"q" read "1"   perform "R, R"                             goto "q"} |:
    {"q" read blank perform "P1, L"                            goto "p"} |:
    {"p" read "x"   perform "E, R"                             goto "q"} |:
    {"p" read "e"   perform "R"                                goto "f"} |:
    {"p" read blank perform "L, L"                             goto "p"} |:
    {"f" read any   perform "R, R"                             goto "f"} |:
    {"f" read blank perform "P0, L, L"                         goto "o"}
  }

  val a1 = Compiler.toAST(t1.mkDSL).toStandardTable
  println(t1.prettyPrint)
  println(t1.run(100))

  val a2 = Compiler.toAST(t2.mkDSL).toStandardTable
  println(t2.prettyPrint)
  println(t2.run(100))

  val a3 = Compiler.toAST(t3.mkDSL).toStandardTable
  println(t3.prettyPrint)
  println(t3.run(10000))
}
