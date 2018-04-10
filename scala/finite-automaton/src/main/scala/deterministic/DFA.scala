package deterministic

import utils._

case class DFA(var currentState: State, acceptStates: Set[State], ruleBook: DFARuleBook) {

  def isInAccept = acceptStates.contains(currentState)

  def readChar(char: Char) = {
    this.currentState = ruleBook.nextState(currentState, char)
  }

  def readString(s: String) = {
    s.foreach(c => readChar(c))
  }
}