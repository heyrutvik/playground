package machine.implicits

import eu.timepit.refined.auto._
import machine.compile.{Compiler, ConfigContext, Symbol, SymbolContext}
import machine.encode.{DescriptionNumber, StandardDescription, StandardForm}
import machine.interpret.Interpreter
import machine.interpret.Interpreter.CompleteConfig
import machine.regular.Table.Entry
import machine.regular.{Table => RTable}
import machine.standard.AST.{Final, Table => ASTTable}
import machine.standard.{AST, S, Table => STable}

object Instance {

  implicit val cc: ConfigContext = Map()
  // TODO drop drop :p
  implicit val sc: SymbolContext =
    Map(Symbol.BLANK -> S(0), "0" -> S(1), "1" -> S(2), Symbol.ANY -> S(3), Symbol.DYNAMIC.drop(1) -> S(4))
  implicit val print: CompleteConfig => Unit = _ => ()

  implicit class StandardFormOps(ast: AST) {
    def toStandardTable: STable = ast match {
      case f: Final => STable(f.toEntry)
      case t: ASTTable => STable(t.toEntry)
    }
  }

  implicit class RTableOps(rt: RTable) {
    def print(step: Int): String = {
      val (_, sc, ast) = Compiler(rt.mkDSL)
      implicit val ct = ast.toStandardTable
      val cs: Map[S, String] = sc.map(_.swap)
      Interpreter.run(step).map(c => cs(c)).map {
        case Symbol.BLANK => "_"
        case s => s
      }.toList.mkString("|", "|", "|")
    }

    def simulate(step: Int): Unit = {
      val (cc, sc, ast) = Compiler(rt.mkDSL)
      implicit val ct = ast.toStandardTable
      val cs = sc.map(_.swap)
      val cc1 = cc.map(_.swap)
      val rewriteSymbol: String => String = {
        case Symbol.BLANK => "_"
        case s => s
      }
      def removePrimes(name: String): String = name.takeWhile(_.isLetter)
      implicit val print: CompleteConfig => Unit = { complete =>
        Console.print {
          fansi.Bold.On(fansi.Color.Green(removePrimes(cc1(complete.state)))) + ": " +
            complete.tape.zipWithIndex.map{
              case (c, i) => (cs(c), i)
            }.map {
              case (sym, i) if i == complete.head =>
                fansi.Back.Red(fansi.Bold.On(fansi.Color.White(rewriteSymbol(sym))))
              case (sym, _) => fansi.Color.Reset(rewriteSymbol(sym))
            }.mkString("|", "|", "|") + "\r"
        }
        Thread.sleep(500)
      }
      println(rt.prettyPrint)
      Interpreter.run(step)
      println
    }
  }

  implicit class TableOps(t: STable) {
    def toStandardForm(implicit ev: StandardForm[STable]): String = ev.encode(t)
    def toStandardDescription(implicit ev: StandardDescription[STable]): String = ev.encode(t)
    def toDescriptionNumber(implicit ev: DescriptionNumber[STable]): String = ev.encode(t)
  }

  implicit class TableNameOps(name: String) {
    def table(es: List[Entry]): RTable = RTable(name, es)
  }
}
