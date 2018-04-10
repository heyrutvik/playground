package deterministic

import utils._
import cats.data._

case class DFADesign(startState: S, acceptState: Set[S], ruleBook: DFARuleBook) {

  def toDFA = DFA(startState, acceptState, ruleBook)

  private def isInAcceptState(s: String) = for {
    _ <- DFA.readString(s)
    dfa <- State.get
  } yield (dfa.isInAccept)

  def isAccept(s: String) = isInAcceptState(s).runA(toDFA).value
}
