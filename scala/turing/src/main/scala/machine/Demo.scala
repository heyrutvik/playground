package machine

import machine.implicits._
import machine.regular.DSL._

object Demo extends App {

  "I. Compute the sequence: 010101010..." table {
    {"b" read blank perform "P0, R" goto "c"} |:
    {"c" read blank perform "R"     goto "e"} |:
    {"e" read blank perform "P1, R" goto "f"} |:
    {"f" read blank perform "R"     goto "b"}
  } simulate 20

  "II. Compute the sequence: 0010110111011110..." table {
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
  } simulate 500
}
