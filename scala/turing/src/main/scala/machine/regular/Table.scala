package machine.regular

import machine.regular.DSL.{Define, Goto, Perform, Read, Table => DSLTable}
import machine.regular.Table.Entry

case class Table(es: List[Entry]) {
  /**
    * TODO
    * @param ls list of regular entries
    * @return DSL
    */
  def mkDSL: DSL = {
    val temp = es.map {
      case Entry(mc, sym, op, fc) => Goto(Perform(Read(Define(mc), sym), op), fc)
    }.reverse
    temp.tail.foldLeft(temp.head: DSL)((dsl, goto) => DSLTable(goto, dsl))
  }

  def prettyPrint: String = {
    val maxop = es.map(e => e.operation.length).max
    val op_padding = "operations".length.max(maxop)
    val padding = "%1$8s | %2$6s | %3$"+op_padding+"s | %4$14s"
    "\n" + padding.format("--------", "------", "----------", "--------------") + "\n" +
    padding.format("m-config", "symbol", "operations", "final m-config") + "\n" +
    padding.format("--------", "------", "----------", "--------------") + "\n" +
    es.map { e =>
      padding.format(e.m_config, if (e.symbol == "") "None" else e.symbol, e.operation, e.final_m_config)
    }.mkString("\n") + "\n" +
    padding.format("--------", "------", "----------", "--------------") + "\n" + "\n"
  }
}

object Table {

  case class Entry(m_config: String, symbol: String, operation: String, final_m_config: String)
}