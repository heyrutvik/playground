package machine.regular

import machine.regular.DSL._

object Demo {

  val t1 = Table {
    {"b" read "" perform "P0, R" goto "c"} |:
    {"c" read "" perform "R"     goto "e"} |:
    {"e" read "" perform "P1, R" goto "f"} |:
    {"f" read "" perform "R"     goto "b"}
  }

  val t2 = Table {
    {"b" read "" perform "P0, R" goto "b"}
  }
}
