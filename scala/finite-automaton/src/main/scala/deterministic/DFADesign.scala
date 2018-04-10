package deterministic

import utils._

case class DFADesign(startState: State, acceptState: Set[State], ruleBook: DFARuleBook) {

  def toDFA = DFA(startState, acceptState, ruleBook)

  def isAccept(s: String) = {
    val dfa = toDFA
    dfa.readString(s)
    dfa.isInAccept
  }
}
