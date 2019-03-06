package machine.regular

import machine.regular.DSL._

object Demo {

  val t1 = Table {
    {"b" read blank perform "P0, R" goto "c"} |:
    {"c" read blank perform "R"     goto "e"} |:
    {"e" read blank perform "P1, R" goto "f"} |:
    {"f" read blank perform "R"     goto "b"}
  }

  val t2 = Table {
    {"b" read blank perform "P0, R" goto "b"}
  }

  val t3 = Table {
    {"o" read "0" perform "" goto "q"} |:
    {"q" read blank perform "P1, L" goto "p"} |:
    {"p" read "x" perform "E, R" goto "q"} |:
    {"p" read "e" perform "R" goto "f"}
  }

  val t4 = Table {
    {"b" read blank perform "Pe, R, Pe, R, P0, R, R, P0, L, L" goto "o"} |:
    {"o" read "1"   perform "R, Px, L, L, L"                   goto "o"} |:
    {"o" read "0"   perform ""                                 goto "q"} |:
    {"q" read "0"   perform "R, R"                             goto "q"} |:
    {"q" read "1"   perform "R, R"                             goto "q"} |:
    {"q" read blank perform "P1, L"                            goto "p"} |:
    {"p" read "x"   perform "E, R"                             goto "q"} |:
    {"p" read "e"   perform "R"                                goto "f"} |:
    {"p" read blank perform "L, L"                             goto "p"} |:
    {"f" read any   perform "R, R"                             goto "f"} |:
    {"f" read blank perform "P0, L, L"                         goto "o"}
  }
}
