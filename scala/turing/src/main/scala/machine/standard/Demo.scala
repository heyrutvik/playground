package machine.standard

import machine.compile.Compiler
import machine.encode.DescriptionNumberInstance._
import machine.encode.StandardDescriptionInstance._
import machine.encode.StandardFormInstance._
import machine.standard.AST._

object Demo extends App {

  val compiler = Compiler()

  val t1 = machine.regular.Demo.t1
  val t2 = machine.regular.Demo.t2
  val t3 = machine.regular.Demo.t3
  val t4 = machine.regular.Demo.t4

  val a1 = compiler.standardForm(t1.mkDSL).toStandardTable
  List(a1.toStandardForm, a1.toStandardDescription, a1.toDescriptionNumber) foreach println

  val a2 = compiler.standardForm(t2.mkDSL).toStandardTable
  List(a2.toStandardForm, a2.toStandardDescription, a2.toDescriptionNumber) foreach println

  val a3 = compiler.standardForm(t3.mkDSL).toStandardTable
  List(a3.toStandardForm, a3.toStandardDescription, a3.toDescriptionNumber) foreach println

  val a4 = compiler.standardForm(t4.mkDSL, true).toStandardTable
  List(t4.prettyPrint, t4.prettyPrintElaborated, a4.toStandardForm, a4.toStandardDescription, a4.toDescriptionNumber) foreach println
}