package machine.standard

import machine.encode.{DescriptionNumber, StandardDescription, StandardForm}
import machine.standard.Table.Entry

case class Table(es: List[Entry])

object Table {
  implicit class TableOps(t: Table) {
    def toStandardForm(implicit ev: StandardForm[Table]): String = ev.encode(t)
    def toStandardDescription(implicit ev: StandardDescription[Table]): String = ev.encode(t)
    def toDescriptionNumber(implicit ev: DescriptionNumber[Table]): String = ev.encode(t)
  }

  case class Entry(name: Q, symbol: S, operation: Operation, next: Q)

  object Entry {
    def sd(e: Entry)(implicit ev: StandardDescription[Entry]): String = ev.encode(e)
  }
}