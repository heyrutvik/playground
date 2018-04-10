package deterministic

import utils._
import cats.data.{State, _}

case class DFA(var currentState: S, acceptStates: Set[S], ruleBook: DFARuleBook) {

  def isInAccept = acceptStates.contains(currentState)
}

object DFA {

  def readChar(char: Char): DFA => DFA =  { dfa =>
    DFA(dfa.ruleBook.nextState(dfa.currentState, char), dfa.acceptStates, dfa.ruleBook)
  }

  def readString(s: String): State[DFA, Unit] = ???
}