package machine

import machine.compile.Compiler
import machine.encode.DescriptionNumberInstance._
import machine.encode.StandardDescriptionInstance._
import machine.encode.StandardFormInstance._
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
    {"b" read blank perform "P0, R" goto "b"}
  }

  val t3 = RTable {
    {"o" read "0"   perform ""      goto "q"} |:
    {"q" read blank perform "P1, L" goto "p"} |:
    {"p" read "x"   perform "E, R"  goto "q"} |:
    {"p" read "e"   perform "R"     goto "f"}
  }

  val t4 = RTable {
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

  val compiler = Compiler()

  val a1 = compiler.standardForm(t1.mkDSL, true).toStandardTable
  List(t1.prettyPrintElaborated, a1.toStandardForm, a1.toStandardDescription, a1.toDescriptionNumber) foreach println

  val a2 = compiler.standardForm(t2.mkDSL).toStandardTable
  List(a2.toStandardForm, a2.toStandardDescription, a2.toDescriptionNumber) foreach println

  val a3 = compiler.standardForm(t3.mkDSL).toStandardTable
  List(a3.toStandardForm, a3.toStandardDescription, a3.toDescriptionNumber) foreach println

  val a4 = compiler.standardForm(t4.mkDSL).toStandardTable
  List(a4.toStandardForm, a4.toStandardDescription, a4.toDescriptionNumber) foreach println
}
