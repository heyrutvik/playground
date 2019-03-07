package machine.standard

import machine.encode.StandardDescription
import machine.standard.Table.Entry

case class Table(es: List[Entry])

object Table {
  case class Entry(name: Q, symbol: S, operation: Operation, next: Q)

  object Entry {
    def sd(e: Entry)(implicit ev: StandardDescription[Entry]): String = ev.encode(e)
  }
}