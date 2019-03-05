package machine.standard

import eu.timepit.refined.auto._
import machine.encode.DescriptionNumberInstance._
import machine.encode.StandardDescriptionInstance._
import machine.encode.StandardFormInstance._
import machine.standard.Table.Entry

object Demo extends App {
  val t1: Table = Table(
    List(
      Entry(q(1), S(0), R(S(1)), q(2)),
      Entry(q(2), S(0), R(S(0)), q(3)),
      Entry(q(3), S(0), R(S(2)), q(4)),
      Entry(q(4), S(0), R(S(0)), q(1))
    )
  )

  println(t1.toStandardForm)
  println(t1.toStandardDescription)
  println(t1.toDescriptionNumber)
}