package machine.regular

import DSL._
import machine.compile._
import machine.compile.Compiler._
import machine.standard.AST._
import machine.encode.StandardFormInstance._

object Demo extends App {

  val t1 = Table {
    {"b" read "" perform "P0, R" goto "c"} |:
    {"c" read "" perform "R"     goto "e"} |:
    {"e" read "" perform "P1, R" goto "f"} |:
    {"f" read "" perform "R"     goto "b"}
  }

  println(standardForm(mkDSL(t1.es))._3.asInstanceOf[machine.standard.AST.Table].toStandardForm.toStandardForm)
}
