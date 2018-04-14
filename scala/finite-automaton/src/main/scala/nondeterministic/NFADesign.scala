package nondeterministic

import cats.data._
import utils._

case class NFADesign(startState: S, acceptState: Set[S], ruleBook: NFARuleBook) {

  val toNFA = NFA(Set(startState), acceptState, ruleBook)

  def isAccept(s: String) = {
    val simulate = for {
      _ <- NFA.readString(s)
      dfa <- State.get
    } yield (dfa.isInAccept)
    simulate.runA(toNFA).value
  }
}
