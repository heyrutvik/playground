package nondeterministic

import cats.data.State
import utils._

case class NFA(var currentStates: Set[S], acceptStates: Set[S], ruleBook: NFARuleBook) {

  def isInAccept: Boolean = currentStates.intersect(acceptStates).size > 0
}

object NFA {

  def readChar(char: Char): NFA => NFA =  { nfa =>
    NFA(nfa.ruleBook.nextStates(nfa.currentStates, char), nfa.acceptStates, nfa.ruleBook)
  }

  def readString(s: String): State[NFA, Unit] = {
    s.map(c => readChar(c)).map(f => State.modify(f)).reduce((s1, s2) => s1.flatMap(_ => s2))
  }
}