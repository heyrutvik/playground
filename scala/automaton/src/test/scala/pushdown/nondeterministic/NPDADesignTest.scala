package pushdown.nondeterministic

import common.UnitTest
import pushdown._
import utils._

class NPDADesignTest extends UnitTest("NPDA Design") {

  "simulate npda using string" should "be in accept state" in {

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

    val npdaDesign = NPDADesign(S(1), '$', Set(S(3)), ruleBook)
    npdaDesign.isAccept("abba") should be (true)
    npdaDesign.isAccept("babbaabbab") should be (true)
    npdaDesign.isAccept("abb") should be (false)
  }
}
