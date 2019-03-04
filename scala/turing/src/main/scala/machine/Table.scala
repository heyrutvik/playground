package machine

import machine.Table.Entry
import machine.encode.{DescriptionNumber, StandardDescription, StandardForm}

case class Table(es: List[Entry])

object Table {
  implicit class TableOps(t: Table) {
    def toStandardForm(implicit ev: StandardForm[Table]): String = ev.encode(t)
    def toStandardDescription(implicit ev: StandardDescription[Table]): String = ev.encode(t)
    def toDescriptionNumber(implicit ev: DescriptionNumber[Table]): String = ev.encode(t)
  }

  case class Entry(m_config: q, symbol: S, operation: Operation, final_m_config: q)

  object Entry {
    def sd(e: Entry)(implicit ev: StandardDescription[Entry]): String = ev.encode(e)
  }
}