package nondeterministic

import common.FARule
import deterministic.UnitTest
import utils._

class NFADesignTest extends UnitTest("NFA Rule Book") {

  "simulate fa using string" should "be in accept state" in {

    val rulebook = NFARuleBook(Seq(
      FARule(S(1), 'a', S(1)), FARule(S(1), 'b', S(1)), FARule(S(1), 'b', S(2)),
      FARule(S(2), 'a', S(3)), FARule(S(2), 'b', S(3)),
      FARule(S(3), 'a', S(4)), FARule(S(3), 'b', S(4))
    ))

    val design = NFADesign(S(1), Set(S(4)), rulebook)
    design.isAccept("bab") should be (true)
    design.isAccept("bbbbb") should be (true)
    design.isAccept("bbabb") should be (false)
  }
}
