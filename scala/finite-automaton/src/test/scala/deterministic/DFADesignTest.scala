package deterministic

import utils._

class DFADesignTest extends UnitTest("DFA Rule Book") {

  "simulate fa using string" should "be in accept state" in {

    val rulebook = DFARuleBook(Seq(
      DFARule(State(1), 'a', State(2)), DFARule(State(1), 'b', State(1)),
      DFARule(State(2), 'a', State(2)), DFARule(State(2), 'b', State(3)),
      DFARule(State(3), 'a', State(3)), DFARule(State(3), 'b', State(3))
    ))

    val design = DFADesign(State(1), Set(State(3)), rulebook)
    design.isAccept("a") should be (false)
    design.isAccept("baa") should be (false)
    design.isAccept("baba") should be (true)
  }
}
