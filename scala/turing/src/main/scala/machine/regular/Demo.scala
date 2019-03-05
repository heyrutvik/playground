package machine.regular

import DSL._

object Demo extends App {

  val t1 = Table {
    {"x" read "" perform "P1, R" goto "y"} |:
    {"y" read "" perform "P2, L" goto "x"} |:
    {"x" read "" perform "P3, R" goto "y"} |:
    {"y" read "" perform "P4, L" goto "x"} |:
    {"x" read "" perform "P5, R" goto "y"} |:
    {"y" read "" perform "P6" goto "x"} |:
    {"x" read "" perform "P7, R" goto "y"} |:
    {"y" read "" perform "P8, L" goto "x"} |:
    {"x" read "" perform "P9, L" goto "z"}
  }

  t1.es.foreach(println)
}
