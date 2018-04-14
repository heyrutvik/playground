package deterministic

import common.FARule
import utils._

class DFADesignTest extends UnitTest("DFA Rule Book") {

  "simulate d fa using string" should "be in accept state" in {

    val rulebook = DFARuleBook(Seq(
      FARule(S(1), 'a', S(2)), FARule(S(1), 'b', S(1)),
      FARule(S(2), 'a', S(2)), FARule(S(2), 'b', S(3)),
      FARule(S(3), 'a', S(3)), FARule(S(3), 'b', S(3))
    ))

    val design = DFADesign(S(1), Set(S(3)), rulebook)
    design.isAccept("a") should be (false)
    design.isAccept("baa") should be (false)
    design.isAccept("bab") should be (true)
    design.isAccept("baba") should be (true)
  }
}
