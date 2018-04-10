package deterministic

import utils._

class DFARuleBookTest extends UnitTest("DFA Rule Book") {

  "check next state" should "be as in rulebook" in {

    val rulebook = DFARuleBook(Seq(
      DFARule(State(1), 'a', State(2)), DFARule(State(1), 'b', State(1)),
      DFARule(State(2), 'a', State(2)), DFARule(State(2), 'b', State(3)),
      DFARule(State(3), 'a', State(3)), DFARule(State(3), 'b', State(3))
    ))

    rulebook.nextState(State(1), 'a') should be (State(2))
    rulebook.nextState(State(1), 'b') should be (State(1))
    rulebook.nextState(State(2), 'b') should be (State(3))
  }
}
