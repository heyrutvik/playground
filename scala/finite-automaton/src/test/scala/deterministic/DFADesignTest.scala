package deterministic

import utils._

class DFADesignTest extends UnitTest("DFA Rule Book") {

  "simulate fa using string" should "be in accept state" in {

    val rulebook = DFARuleBook(Seq(
      DFARule(S(1), 'a', S(2)), DFARule(S(1), 'b', S(1)),
      DFARule(S(2), 'a', S(2)), DFARule(S(2), 'b', S(3)),
      DFARule(S(3), 'a', S(3)), DFARule(S(3), 'b', S(3))
    ))

    val design = DFADesign(S(1), Set(S(3)), rulebook)
    design.isAccept("a") should be (false)
    design.isAccept("baa") should be (false)
    design.isAccept("bab") should be (true)
    design.isAccept("baba") should be (true)
  }
}
