package machine.regular

import machine.regular.Table.Entry

case class Table(es: List[Entry])

object Table {

  case class Entry(m_config: String, symbol: String, operation: String, final_m_config: String)
}