package machine

import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.NonNegative
import machine.compile.{Compiler, ConfigContext, Keyword, Symbol, SymbolContext}
import machine.encode.{DescriptionNumber, StandardDescription, StandardForm}
import machine.interpret.Interpreter
import machine.interpret.Interpreter.CompleteConfig
import machine.regular.Table.Entry
import machine.regular.{Table => RTable}
import machine.standard.AST.{FC, Table => ASTTable}
import machine.standard.{AST, S, Table => STable}

package object implicits {

  implicit val cc: ConfigContext = Map()
  // TODO drop drop :p
  implicit val sc: SymbolContext =
    Map(Keyword.BLANK -> S(0), "0" -> S(1), "1" -> S(2), Keyword.ANY -> S(3), Symbol.DYNAMIC.drop(1) -> S(4))
  implicit val print: CompleteConfig => Unit = _ => ()

  implicit class StandardFormOps(ast: AST) {
    def toStandardTable: STable = ast match {
      case f: FC => STable(f.toEntry)
      case t: ASTTable => STable(t.toEntry)
    }
  }

  implicit class RTableOps(rt: RTable) {
    def print(step: Int Refined NonNegative): Unit = {
      val (_, sc, ast) = Compiler(rt.mkDSL)
      implicit val ct = ast.toStandardTable
      val cs: Map[S, String] = sc.map(_.swap)
      Console.print {
        Interpreter.run(step).map(c => cs(c)).map {
          case Keyword.BLANK => "_"
          case s => s
        }.toList.mkString("|", "|", "|")
      }
    }

    def simulate(step: Int Refined NonNegative): Unit = {
      val (cc, sc, ast) = Compiler(rt.mkDSL)
      implicit val ct = ast.toStandardTable
      val cs = sc.map(_.swap)
      val cc1 = cc.map(_.swap)
      val rewriteSymbol: String => String = {
        case Keyword.BLANK => "_"
        case s => s
      }
      implicit val print: CompleteConfig => Unit = { complete =>
        Console.print {
          fansi.Bold.On(fansi.Color.Green(machine.elaborate.removePrimes(cc1(complete.state)))) + ": " +
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
      println(rt.pretty)
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
