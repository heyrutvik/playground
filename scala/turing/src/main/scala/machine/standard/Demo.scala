package machine.standard

import machine.compile.Compiler.standardForm
import machine.compile._
import machine.encode.DescriptionNumberInstance._
import machine.encode.StandardDescriptionInstance._
import machine.encode.StandardFormInstance._
import machine.standard.AST._

object Demo extends App {

  val t1 = machine.regular.Demo.t1
  val t2 = machine.regular.Demo.t2

  val t3 = standardForm(t1.mkDSL)._3.toStandardTable
  List(t1.prettyPrint, t3.toStandardForm, t3.toStandardDescription, t3.toDescriptionNumber) foreach println

  val t4 = standardForm(t2.mkDSL)._3.toStandardTable
  List(t2.prettyPrint, t4.toStandardForm, t4.toStandardDescription, t4.toDescriptionNumber) foreach println
}