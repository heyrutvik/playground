package deterministic

import utils._

class DFARuleBookTest extends UnitTest("DFA Rule Book") {

  "check next state" should "be as in rulebook" in {

    val rulebook = DFARuleBook(Seq(
      DFARule(S(1), 'a', S(2)), DFARule(S(1), 'b', S(1)),
      DFARule(S(2), 'a', S(2)), DFARule(S(2), 'b', S(3)),
      DFARule(S(3), 'a', S(3)), DFARule(S(3), 'b', S(3))
    ))

    rulebook.nextState(S(1), 'a') should be (S(2))
    rulebook.nextState(S(1), 'b') should be (S(1))
    rulebook.nextState(S(2), 'b') should be (S(3))
  }
}