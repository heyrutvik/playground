package finite.nondeterministic

import cats.data.State
import utils._

case class NFA(var cs: Set[S], acceptStates: Set[S], ruleBook: NFARuleBook) {
  val currentStates = ruleBook.followFreeMoves(cs)
  def isInAccept: Boolean = currentStates.intersect(acceptStates).nonEmpty
}

object NFA {

  def readChar(char: Char): NFA => NFA =  { nfa =>
    NFA(nfa.ruleBook.nextStates(nfa.currentStates, char), nfa.acceptStates, nfa.ruleBook)
  }

  def readString(s: String): State[NFA, Unit] = {
    s.map(c => readChar(c)).map(f => State.modify(f)).reduce((s1, s2) => s1.flatMap(_ => s2))
  }
}