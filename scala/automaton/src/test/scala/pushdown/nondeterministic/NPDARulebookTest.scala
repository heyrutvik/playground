package pushdown.nondeterministic

import common.UnitTest
import pushdown._
import utils._

class NPDARulebookTest extends UnitTest("NPDA Rule Book") {

  "npda rule book" should "" in {

    val ruleBook = NPDARuleBook(Seq(
      PDARule(S(1), 'a', S(1), '$', List('a', '$')),
      PDARule(S(1), 'a', S(1), 'a', List('a', 'a')),
      PDARule(S(1), 'a', S(1), 'b', List('a', 'b')),

      PDARule(S(1), 'b', S(1), '$', List('b', '$')),
      PDARule(S(1), 'b', S(1), 'a', List('b', 'a')),
      PDARule(S(1), 'b', S(1), 'b', List('b', 'b')),

      PDARule(S(1), ' ', S(2), '$', List('$')),
      PDARule(S(1), ' ', S(2), 'a', List('a')),
      PDARule(S(1), ' ', S(2), 'b', List('b')),

      PDARule(S(2), 'a', S(2), 'a', Nil),
      PDARule(S(2), 'b', S(2), 'b', Nil),
      PDARule(S(2), ' ', S(3), '$', List('$'))
    ))

    val config = PDAConfig(S(1), Stack(List('$')))
    val npda = NPDA(Set(config), Set(S(3)), ruleBook)
    npda.isInAccept should be (true)
//    println("NPDA: " + npda.currentConfigs)
    val step1 = NPDA.readString("abb").runS(npda).value
//    println("STEP1: " + step1.currentConfigs)
    step1.isInAccept should be (false)
    val step2 = NPDA.readString("abba").runS(npda).value
//    println("STEP2: " + step2.currentConfigs)
    step2.isInAccept should be (true)
  }
}

